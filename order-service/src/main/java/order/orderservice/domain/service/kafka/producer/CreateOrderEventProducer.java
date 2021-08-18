package order.orderservice.domain.service.kafka.producer;

import static java.time.LocalDateTime.now;
import static order.orderservice.util.Constant.Kafka.ORDER_TOPIC;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CreateOrderEventProducer {

    @Resource
    private KafkaTemplate<String, KafkaOrderEvent> kafkaTemplate;
    @Resource
    private Mapper mapper;

    public void sendMessage(Order order) {
        KafkaOrderEvent orderEvent = mapper.map(order, KafkaOrderEvent.class);
        orderEvent.setCreateAt(now());
        orderEvent.setCreateBy("order-service");
        orderEvent.setOperationType("CREATE");
        kafkaTemplate.send(ORDER_TOPIC, orderEvent);
    }
}
