package org.tms.task_executor_service.domain.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.annotation.ThreadSaveMethod;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

@Component
public class TaskExecutionServiceImp implements TaskExecutionService, ThreadSaveResource{

    @Getter
    private final Lock lock = new ReentrantLock();
    @Resource
    private TaskRepository taskRepository;

    @ThreadSaveMethod
    @Override
    public List<Task> executeTasks(Integer queueSize) {
        return null;
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
}