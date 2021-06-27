package order.orderservice.configuration.execution;

import static java.lang.Thread.currentThread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.Callable;

@Slf4j
@Component
public class AsyncContextDataDecorator implements CallableTaskDecorator {

    @Resource
    private ApplicationContext appContext;

    @Override
    public <V> Callable<V> decorate(Callable<V> callable) {
        //TODO: can do some preparation here. Perform in common thread.
        log.info("{} - preparation.", currentThread().getName());
        return () -> {
            //TODO: can do some extra(decorated) logics before main logics(call). Perform in privat thread.
            log.info("{} - decorated logics.", currentThread().getName());
            return callable.call();
        };
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        //TODO: can do some preparation here. Perform in common thread.
        log.info("{} - preparation.", currentThread().getName());
        return () -> {
            //TODO: can do some extra(decorated) logics before main logics(run). Perform in privat thread.
            log.info("{} - decorated logics.", currentThread().getName());
            runnable.run();
        };
    }
}
