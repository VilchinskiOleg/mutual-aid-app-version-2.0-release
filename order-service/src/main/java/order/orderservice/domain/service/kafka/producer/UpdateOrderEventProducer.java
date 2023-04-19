package order.orderservice.domain.service.kafka.producer;

import com.mongodb.client.model.changestream.OperationType;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderEventProducer extends OrderEventProducer {

    @Override
    public OperationType getOperation() {
        return OperationType.UPDATE;
    }
}