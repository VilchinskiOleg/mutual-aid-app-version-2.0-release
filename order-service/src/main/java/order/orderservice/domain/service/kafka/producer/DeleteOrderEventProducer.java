package order.orderservice.domain.service.kafka.producer;

import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType.DELETE;
import static order.orderservice.util.Constant.Kafka.ORDER_TOPIC;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class DeleteOrderEventProducer extends OrderEventProducer {

    @Override
    public KafkaOrderEvent.OperationType getOperation() {
        return DELETE;
    }

    @Override
    public void sendMessage(Order order) {
        KafkaOrderEvent orderEvent = new KafkaOrderEvent(order.getOrderId());
        send(orderEvent);
    }
}