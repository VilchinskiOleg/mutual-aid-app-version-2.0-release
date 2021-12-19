package org.tms.task_executor_service.domain.service;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.PageRequest.of;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.annotation.ThreadSaveMethod;
import org.tms.task_executor_service.domain.service.command.Command;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

@Component
public class TaskExecutionServiceImp implements TaskExecutionService, ThreadSaveResource{

    @Getter
    private final Lock lock = new ReentrantLock();

    @Resource
    private TaskRepository taskRepository;
    @Resource
    private CommandExecutor commandExecutor;
    @Resource
    private CommandManager commandManager;
    @Resource
    private Mapper mapper;

    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        var tasksPage = taskRepository.findAll(pageRequest).map(task -> mapper.map(task, Task.class));

        return executeTasks(tasksPage.getContent());
    }

    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Set<String> taskIds) {
        return null;
    }

    @ThreadSaveMethod(lockTimeOut = 2)
    @Override
    public List<Task> getTasks(Integer queueSize) {
        return null;
    }

    private List<Task> executeTasks(List<Task> tasks) {
        return tasks.stream()
                    .map(commandManager::retrieveCommand)
                    .filter(commandExecutor::execute)
                    .map(Command::getTask)
                    .collect(toList());
    }
}