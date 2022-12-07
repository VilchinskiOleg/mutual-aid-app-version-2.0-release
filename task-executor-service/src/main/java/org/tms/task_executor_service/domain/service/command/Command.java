package org.tms.task_executor_service.domain.service.command;

import org.tms.task_executor_service.domain.model.Task;

/**
 * All implementations must have only single constructor with 3 args (task, mapper, client)
 */
public interface Command {

    /**
     * Main method:
     * @return true if task executes success
     */
    void apply();

    Task getTask();

    String getName();

    boolean isSuccessful();
}