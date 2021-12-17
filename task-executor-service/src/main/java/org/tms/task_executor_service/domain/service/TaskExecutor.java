package org.tms.task_executor_service.domain.service;

import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.command.BaseCommand;

@Component
public class TaskExecutor {

    public boolean execute(BaseCommand command) {
        //TODO
        boolean success = command.apply();
        //TODO (send notification?)
        return success;
    }
}