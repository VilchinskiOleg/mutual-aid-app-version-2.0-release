package messagechat.messagechatservice.configuration.execution;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolTaskExecutionConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor getExecutorService() {
        var executorService = new ThreadPoolTaskExecutor();
        executorService.setCorePoolSize(10);
        executorService.setMaxPoolSize(20);
        executorService.initialize();
        return executorService;
    }
}