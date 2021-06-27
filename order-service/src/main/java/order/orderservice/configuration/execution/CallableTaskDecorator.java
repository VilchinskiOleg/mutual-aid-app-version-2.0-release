package order.orderservice.configuration.execution;

import org.springframework.core.task.TaskDecorator;
import java.util.concurrent.Callable;

public interface CallableTaskDecorator extends TaskDecorator {

    <V> Callable<V> decorate(Callable<V> callable);
}
