package org.tms.task_executor_service.domain.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.TaskExecutionService;
import javax.annotation.Resource;

@Slf4j
@Component
public class TaskExecutionJob {

    @Resource
    private TaskExecutionService taskExecutionService;

    public void execute() {
    }
}