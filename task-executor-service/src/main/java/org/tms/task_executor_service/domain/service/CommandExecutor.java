package org.tms.task_executor_service.domain.service;

import static org.tms.task_executor_service.utils.Constant.Service.NANO_TO_MILlI_RESOLVER;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.command.Command;

@Slf4j
@Component
public class CommandExecutor {

    public boolean execute(Command command) {
        log.info("Execute task={}", command.getTask());
        long startTime = System.nanoTime();
        boolean success = command.apply();
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) * NANO_TO_MILlI_RESOLVER;
        loggingResult(success, command, executionTime);
        //TODO: send notification for user?
        return success;
    }

    private void loggingResult(boolean success, Command command, double executionTime) {
        if (success) {
            log.info("Successfully execute command={} with task={}, execution time={}", command.getName(), command.getTask(), executionTime);
        } else {
            log.warn("Fail execute command={} with task={}, execution time={}", command.getName(), command.getTask(), executionTime);
        }
    }
}