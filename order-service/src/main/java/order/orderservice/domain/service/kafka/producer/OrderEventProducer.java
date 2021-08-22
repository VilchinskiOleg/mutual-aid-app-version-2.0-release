package order.orderservice.domain.service.kafka.producer;

import static java.time.LocalDateTime.now;
import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import static order.orderservice.util.Constant.Kafka.ORDER_CLIENT_NAME;
import static order.orderservice.util.Constant.Kafka.ORDER_TOPIC;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.kafka.core.KafkaTemplate;
import javax.annotation.Resource;

public abstract class OrderEventProducer {

    @Resource
    protected KafkaTemplate<String, KafkaOrderEvent> kafkaTemplate;
    @Resource
    private Mapper mapper;

    public void sendMessage(Order order) {
        KafkaOrderEvent orderEvent = mapper.map(order, KafkaOrderEvent.class);
        populateOrderEvent(orderEvent);
        kafkaTemplate.send(ORDER_TOPIC, orderEvent);
    }

    public abstract OperationType getOperation();

    protected void populateOrderEvent(KafkaOrderEvent orderEvent) {
        orderEvent.setCreateAt(now());
        orderEvent.setCreateBy(ORDER_CLIENT_NAME);
        orderEvent.setOperationType(getOperation());
    }
}