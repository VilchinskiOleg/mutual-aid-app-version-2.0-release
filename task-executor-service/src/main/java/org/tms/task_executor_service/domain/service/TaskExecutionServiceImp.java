package org.tms.task_executor_service.domain.service;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.PageRequest.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.command.Command;
import org.tms.task_executor_service.persistent.repository.TaskRepository;
import org.tms.thread_save.thread_save_api.annotation.ThreadSaveMethod;
import org.tms.thread_save.thread_save_api.marker.ThreadSaveResource;

@Component
public class TaskExecutionServiceImp implements TaskExecutionService, ThreadSaveResource {

    @Getter
    private final Lock lock = new ReentrantLock();

    @Resource
    private TaskRepository taskRepository;
    @Resource
    private CommandExecutor commandExecutor;
    @Resource
    private CommandManager commandManager;
    @Resource
    private ExecutorService asyncTaskExecutorService;
    @Resource
    private Mapper mapper;

    @Nullable
    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        var tasksPage = taskRepository.findAll(pageRequest)
                                                 .map(task -> mapper.map(task, Task.class));

        List<Task> successfulTasks = executeTasks(tasksPage.getContent());
        taskRepository.removeTasks(successfulTasks.stream()
                                                  .map(Task::getInternalId)
                                                  .collect(toList()));
        return successfulTasks;
    }

    @Nullable
    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Set<String> taskIds) {
        var dataTasks = taskRepository.findAllByInternalIds(taskIds);
        List<Task> tasks = mapper.map(dataTasks, new ArrayList<>(), Task.class);
        List<Task> successfulTasks = executeTasks(tasks);
        taskRepository.removeTasks(successfulTasks.stream()
                                                  .map(Task::getInternalId)
                                                  .collect(toList()));
        return successfulTasks;
    }

    @Nullable
    @ThreadSaveMethod(lockTimeOut = 2)
    @Override
    public List<Task> getTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        return taskRepository.findAll(pageRequest)
                             .map(task -> mapper.map(task, Task.class))
                             .getContent();
    }

    private List<Task> executeTasks(List<Task> tasks) {
        List<CompletableFuture<Command>> commandFutures = tasks.stream()
                                                         .map(this::executeTask)
                                                         .collect(toList());
        return CompletableFuture.allOf(commandFutures.toArray(new CompletableFuture[commandFutures.size()]))
                                .thenApply(future -> commandFutures.stream()
                                                                   .map(CompletableFuture::join)
                                                                   .collect(toList())
                                )
                                .thenApply(completedCommands -> completedCommands
                                        .stream()
                                        .filter(Command::isSuccessful)
                                        .map(Command::getTask)
                                        .collect(toList()))
                                .join();//todo: check unchecked exceptions for join()!
    }

    private CompletableFuture<Command> executeTask(Task task) {
        return CompletableFuture.supplyAsync(() -> {
            Command command = commandManager.retrieveCommand(task);
            commandExecutor.execute(command);
            return command;
        }, asyncTaskExecutorService);
    }
}