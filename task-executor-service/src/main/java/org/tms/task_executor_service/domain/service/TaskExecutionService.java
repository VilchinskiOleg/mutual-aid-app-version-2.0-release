package org.tms.task_executor_service.domain.service;

import org.tms.task_executor_service.domain.model.Task;
import java.util.List;

public interface TaskExecutionService {

    List<Task> executeTasks(Integer queueSize);

    List<Task> executeTasks(List<String> taskIds);

    List<Task> getTasks(Integer queueSize);
}