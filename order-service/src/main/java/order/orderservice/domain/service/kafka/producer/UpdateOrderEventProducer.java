package order.orderservice.domain.service.kafka.producer;

import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType.UPDATE;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderEventProducer extends OrderEventProducer {

    @Override
    public KafkaOrderEvent.OperationType getOperation() {
        return UPDATE;
    }
}