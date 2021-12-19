package org.tms.task_executor_service.domain.service.command;

import org.tms.task_executor_service.domain.model.Task;

/**
 * All implementations must have only single constructor with all args
 */
public interface Command {

    /**
     * Main method:
     * @return true if task executes success
     */
    boolean apply();

    Task getTask();

    String getName();
}