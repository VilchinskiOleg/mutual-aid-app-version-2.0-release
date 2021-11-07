package org.tms.task_executor_service.domain.service.sheduling;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.TaskExecutionService;

@Component
@Slf4j
public class TaskExecutionJob {

    @Resource
    private TaskExecutionService taskExecutionService;

    @Scheduled(cron = "${scheduler.remove-closed-orders-job.cron}")
    public void execute() {

    }
}