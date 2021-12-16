package org.tms.task_executor_service.domain.service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

@Component
public class TaskExecutionService {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @Resource
    private TaskRepository taskRepository;

    public void executeTasks(Integer queueSize) {

    }

    public void executeTasks(List<String> taskIds) {

    }

    public void executeTask(String taskId) {

    }

    public List<Task> getTasks(Integer queueSize) {
        return null;
    }
}