package org.tms.task_executor_service.config.sheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.tms.task_executor_service.domain.service.scheduling.TaskExecutionJob;

import java.util.Date;
import java.util.concurrent.Executor;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig implements SchedulingConfigurer {

    @Value("${scheduling-props.period}")
    private long period;
    private final TaskExecutionJob taskExecutionJob;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledExecutor());
        taskRegistrar.addTriggerTask(() -> taskExecutionJob.execute(),
                                     triggerContext -> {
                                         var lastCompletion = ofNullable(triggerContext.lastCompletionTime());
                                         return Date.from(lastCompletion.orElse(new Date())
                                                                        .toInstant()
                                                                        .plusSeconds(getExecutionPeriodBySecond()));
                                     });
    }


    private Executor scheduledExecutor() {
        return newSingleThreadScheduledExecutor();
    }

    private long getExecutionPeriodBySecond() {
        return period * 60;
    }
}