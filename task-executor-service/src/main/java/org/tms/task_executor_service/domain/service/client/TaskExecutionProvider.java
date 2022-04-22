package org.tms.task_executor_service.domain.service.client;

import java.util.Set;
import org.tms.task_executor_service.domain.model.Task.Type;

public interface TaskExecutionProvider<T> {

    Set<Type> getSupportedTasks();

    T getProvider();
}