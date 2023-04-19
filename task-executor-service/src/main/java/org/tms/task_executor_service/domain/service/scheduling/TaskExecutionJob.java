package org.tms.task_executor_service.domain.service.scheduling;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.TaskExecutionService;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
@AllArgsConstructor
public class TaskExecutionJob {

    @Value("${scheduling-props.tasks-amount}")
    private static int TASKS_AMOUNT;
    private TaskExecutionService taskExecutionService;

    public void execute() {
        log.info("Start job = {}, dateTime = {}", jobName(), now());
        try {
            taskExecutionService.executeTasks(TASKS_AMOUNT);
        } catch (Exception ex) {
            log.error("Unexpected error while execution job = {}", jobName(), ex);
        } finally {
            log.info("Finished job = {}, dateTime = {}", jobName(), now());
        }
    }

    private String jobName() {
        return this.getClass().getSimpleName();
    }
}