package order.orderservice.domain.service.processor;

import static java.util.Optional.ofNullable;
import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import static java.util.stream.Collectors.toMap;
import static order.orderservice.util.Constant.Errors.KAFKA_PRODUCER_NOT_FUND;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.service.kafka.producer.OrderEventProducer;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class EventManagerService {

    private final Map<OperationType, OrderEventProducer> orderEventProducersByOperationType;

    public EventManagerService(List<OrderEventProducer> orderEventProducers) {
        this.orderEventProducersByOperationType = orderEventProducers.stream()
                .collect(toMap(OrderEventProducer::getOperation, producer -> producer));
    }

    public void sendEvent(OperationType operationType, Order order) {
        getEventProducerByOperation(operationType).sendMessage(order);
    }

    private OrderEventProducer getEventProducerByOperation(OperationType operationType) {
        return ofNullable(orderEventProducersByOperationType.get(operationType))
                .orElseThrow(() -> new ConflictException(KAFKA_PRODUCER_NOT_FUND));
    }
}
