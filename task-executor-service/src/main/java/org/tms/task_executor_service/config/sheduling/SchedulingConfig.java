package org.tms.task_executor_service.config.sheduling;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.util.Date;
import java.util.concurrent.Executor;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tms.task_executor_service.config.TaskExecutionProperties;
import org.tms.task_executor_service.domain.service.scheduling.TaskExecutionJob;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    @Resource
    private TaskExecutionJob taskExecutionJob;
    @Resource
    private TaskExecutionProperties properties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledExecutor());
        taskRegistrar.addTriggerTask(() -> taskExecutionJob.execute(),
                                     triggerContext -> {
                                         var lastCompletion = ofNullable(triggerContext.lastCompletionTime());
                                         return Date.from(lastCompletion.orElse(new Date())
                                                                        .toInstant()
                                                                        .plusSeconds(properties.getExecutionPeriodBySecond()));
                                     });
    }

    private Executor scheduledExecutor() {
        return newSingleThreadScheduledExecutor();
    }
}