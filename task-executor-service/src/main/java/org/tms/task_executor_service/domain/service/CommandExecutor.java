package org.tms.task_executor_service.domain.service;

import static org.tms.task_executor_service.utils.Constant.Service.NANO_TO_MILlI_RESOLVER;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.command.Command;

@Slf4j
@Component
public class CommandExecutor {

    public void execute(Command command) {
        log.info("Execute task={}", command.getTask());
        long startTime = System.nanoTime();
        command.apply();
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) * NANO_TO_MILlI_RESOLVER;
        loggingResult(command, executionTime);

        //TODO: send notification for user?
    }

    private void loggingResult(Command command, double executionTime) {
        if (command.isSuccessful()) {
            log.info("Successfully execute command={} with task={}, execution time={}", command.getName(), command.getTask(), executionTime);
        } else {
            log.warn("Fail execute command={} with task={}, execution time={}", command.getName(), command.getTask(), executionTime);
        }
    }
}