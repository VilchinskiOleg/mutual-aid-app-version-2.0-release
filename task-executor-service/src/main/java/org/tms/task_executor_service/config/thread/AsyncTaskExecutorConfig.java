package org.tms.task_executor_service.config.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tms.task_executor_service.config.TaskExecutionProperties;

@Configuration
public class AsyncTaskExecutorConfig {

    @Resource
    private TaskExecutionProperties properties;

    @Bean(name = "asyncTaskExecutor")
    public ExecutorService asyncTaskExecutor() {
        return Executors.newFixedThreadPool(properties.getPoolSize());
    }
}