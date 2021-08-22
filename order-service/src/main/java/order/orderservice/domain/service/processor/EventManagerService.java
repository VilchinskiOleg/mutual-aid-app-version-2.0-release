package order.orderservice.domain.service.processor;

import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import static order.orderservice.util.Constant.Errors.KAFKA_PRODUCER_NOT_FUND;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.service.kafka.producer.OrderEventProducer;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

@Component
public class EventManagerService {

    @Resource
    private List<OrderEventProducer> orderEventProducers;

    public void sendEvent(OperationType operationType, Order order) {
        getEventProducerByOperation(operationType).sendMessage(order);
    }

    private OrderEventProducer getEventProducerByOperation(OperationType operationType) {
        return orderEventProducers.stream()
                .filter(producer -> producer.getOperation() == operationType)
                .findFirst()
                .orElseThrow(() -> new ConflictException(KAFKA_PRODUCER_NOT_FUND));
    }
}
