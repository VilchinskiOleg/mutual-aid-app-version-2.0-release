package order.orderservice.domain.service.kafka.producer;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.persistent.mongo.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class DeleteOrderEventProducer extends OrderEventProducer {

    @Override
    public OperationType getOperation() {
        return OperationType.DELETE;
    }

    @Override
    public void sendMessage(ChangeStreamDocument<Order> changeStreamDoc) {
        final String mongoOrderId = changeStreamDoc.getDocumentKey().getFirstKey(); //TODO: it isn't OrderId (UUID), it is MongoOrderId [*] !
        KafkaOrderEvent orderEvent = new KafkaOrderEvent(mongoOrderId);
        send(orderEvent);
    }
}