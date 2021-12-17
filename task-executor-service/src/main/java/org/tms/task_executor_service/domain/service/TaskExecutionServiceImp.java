package org.tms.task_executor_service.domain.service;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.PageRequest.of;
import static org.tms.task_executor_service.domain.model.Task.Type.CREATE_PROFILE;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.annotation.ThreadSaveMethod;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import org.tms.task_executor_service.domain.service.command.BaseCommand;
import org.tms.task_executor_service.domain.service.command.CreateProfileCommand;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

@Component
public class TaskExecutionServiceImp implements TaskExecutionService, ThreadSaveResource{

    @Getter
    private final Lock lock = new ReentrantLock();

    @Resource
    private TaskRepository taskRepository;
    @Resource
    private TaskExecutor taskExecutor;
    @Resource
    private Mapper mapper;

    @Resource
    private ProfileClientService profileClientService;

    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        var tasksPage = taskRepository.findAll(pageRequest).map(task -> mapper.map(task, Task.class));
        var commands = tasksPage.get().map(this::matchToCommand).filter(Objects::nonNull).collect(toList());

        return commands.stream().filter(taskExecutor::execute).map(this::matchToTask).collect(toList());
    }

    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(List<String> taskIds) {
        return null;
    }

    @ThreadSaveMethod(lockTimeOut = 2)
    @Override
    public List<Task> getTasks(Integer queueSize) {
        return null;
    }

    private BaseCommand matchToCommand(Task task) {
        //TODO: migrate to single class manager:
        if (CREATE_PROFILE == task.getType()) {
            return new CreateProfileCommand(task.getPayload(), profileClientService, mapper);
        }
        //TODO
        return null;
    }

    private Task matchToTask(BaseCommand command) {
        //TODO
        return null;
    }
}