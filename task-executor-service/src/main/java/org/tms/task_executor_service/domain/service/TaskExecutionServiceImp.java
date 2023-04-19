package org.tms.task_executor_service.domain.service;

import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.dto.Event;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.persistent.repository.TaskRepository;
import org.tms.thread_save.thread_save_api.annotation.ThreadSaveMethod;
import org.tms.thread_save.thread_save_api.marker.ThreadSaveResource;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class TaskExecutionServiceImp implements TaskExecutionService, ThreadSaveResource {

    @Getter
    private final Lock lock = new ReentrantLock();

    @Resource
    private TaskRepository taskRepository;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private InitializationCommandManager initializationCommandManager;
    @Resource
    private Mapper mapper;

    @Nullable
    @ThreadSaveMethod(lockTimeOut = 2)
    @Override
    public void executeTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        var tasksPage = taskRepository.findAll(pageRequest)
                                      .map(task -> mapper.map(task, Task.class));

        generateEvents(tasksPage.getContent()).forEach(event -> eventPublisher.sendEvent(event));
    }

    @Nullable
    @ThreadSaveMethod(lockTimeOut = 2)
    @Override
    public void executeTasks(Set<String> taskIds) {
        var dataTasks = taskRepository.findAllByInternalIds(taskIds);
        List<Task> tasks = mapper.map(dataTasks, new ArrayList<>(), Task.class);

        generateEvents(tasks).forEach(event -> eventPublisher.sendEvent(event));
    }

    @Nullable
    @ThreadSaveMethod
    @Override
    public List<Task> getTasks(Integer queueSize) {
        var pageRequest = of(INTEGER_ZERO, queueSize);
        return taskRepository.findAll(pageRequest)
                             .map(task -> mapper.map(task, Task.class))
                             .getContent();
    }


    private List<Event> generateEvents(List<Task> tasks) {
        Map<Class<?>, List<Object>> commandByListener = tasks.stream()
                .collect(groupingBy(
                        task -> task.getType().getCommandsGroupListenerImplClass(),
                        mapping(task -> initializationCommandManager.retrieveCommand(task), toList())
                ));
        return commandByListener.entrySet().stream()
                .map(entry -> new Event(
                        entry.getKey(),
                        new TreeSet<>(entry.getValue())
                ))
                .collect(toList());
    }
}