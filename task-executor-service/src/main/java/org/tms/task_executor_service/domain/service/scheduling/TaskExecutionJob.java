package org.tms.task_executor_service.domain.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.config.TaskExecutionProperties;
import org.tms.task_executor_service.domain.service.TaskExecutionService;
import javax.annotation.Resource;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class TaskExecutionJob {

    @Resource
    private TaskExecutionService taskExecutionService;
    @Resource
    private TaskExecutionProperties properties;

    public void execute() {
        log.info("Start job = {}, dateTime = {}", jobName(), now());
        try {
            var tasks = taskExecutionService.executeTasks(properties.getExecutionTasksAmountByJob());
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