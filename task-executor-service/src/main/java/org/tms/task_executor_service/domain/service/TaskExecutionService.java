package org.tms.task_executor_service.domain.service;

import org.tms.task_executor_service.domain.model.Task;

import java.util.List;
import java.util.Set;

public interface TaskExecutionService {

    void executeTasks(Integer queueSize);

    void executeTasks(Set<String> taskIds);

    List<Task> getTasks(Integer queueSize);
}