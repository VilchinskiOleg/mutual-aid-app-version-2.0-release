package org.tms.task_executor_service.config;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "task-execution-props")
public class TaskExecutionProperties {

    private LocalDateTime date;
    private long period; //min
    private int poolSize;

    public long getExecutionPeriodBySecond() {
        return period * 60;
    }

    public Integer getPoolSize() {
        return poolSize;
    }
}