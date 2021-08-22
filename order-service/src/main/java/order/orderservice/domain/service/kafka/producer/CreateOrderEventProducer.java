package order.orderservice.domain.service.kafka.producer;

import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType.CREATE;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderEventProducer extends OrderEventProducer {

    @Override
    public KafkaOrderEvent.OperationType getOperation() {
        return CREATE;
    }
}