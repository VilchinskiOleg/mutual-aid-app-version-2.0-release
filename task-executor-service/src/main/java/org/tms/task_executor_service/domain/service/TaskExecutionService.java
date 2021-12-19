package org.tms.task_executor_service.domain.service;

import java.util.List;
import java.util.Set;
import org.tms.task_executor_service.domain.model.Task;

public interface TaskExecutionService {

    List<Task> executeTasks(Integer queueSize);

    List<Task> executeTasks(Set<String> taskIds);

    List<Task> getTasks(Integer queueSize);
}