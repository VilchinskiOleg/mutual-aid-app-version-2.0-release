package order.orderservice.configuration.execution;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
/**
 *  Need for use with @Async annotation:
 */
//@EnableAsync(mode = AdviceMode.ASPECTJ)
public class ThreadPoolExecutionConfig {

    /**
     * for b) case.
     */
    @Resource
    private CallableTaskDecorator asyncContextDataDecorator;

    /**
     * a) Use if you need simple thread pool executor.
     */
    @Bean(name = "executorService")
    public ExecutorService getExecutor(){
        return Executors.newFixedThreadPool(10);
    }

    /**
     * b) Use if you wont decorate some action before run thread.
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor getExecutorService() {
        var executorService = new ThreadPoolTaskExecutor();
        executorService.setCorePoolSize(10);
        executorService.setMaxPoolSize(20);
        executorService.setTaskDecorator(asyncContextDataDecorator);
        executorService.initialize();
        return executorService;
    }
}
