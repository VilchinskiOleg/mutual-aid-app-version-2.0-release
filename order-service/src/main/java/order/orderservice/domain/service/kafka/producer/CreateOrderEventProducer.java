package order.orderservice.domain.service.kafka.producer;

import static order.orderservice.util.Constant.Kafka.ORDER_TOPIC;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CreateOrderEventProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String orderId) {
        kafkaTemplate.send(ORDER_TOPIC, orderId);
    }
}
