package order.orderservice.domain.service.processor;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import lombok.extern.slf4j.Slf4j;
import order.orderservice.domain.service.kafka.producer.OrderEventProducer;
import order.orderservice.persistent.mongo.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
public class EventManagerService {

    private final Map<OperationType, OrderEventProducer> orderEventProducersByOperationType;

    public EventManagerService(List<OrderEventProducer> orderEventProducers) {
        this.orderEventProducersByOperationType = orderEventProducers.stream()
                .collect(toMap(OrderEventProducer::getOperation, producer -> producer));
    }


    public void processEvent(ChangeStreamDocument<Order> changeStreamDoc) {
        final var operationType = changeStreamDoc.getOperationType();
        final var order = changeStreamDoc.getFullDocument();
        log.info("Process data change event:\n operationType ={},\n mongoOrderId ={},\n orderId ={},\n order ={}",
                operationType,
                requireNonNull(changeStreamDoc.getDocumentKey()).getFirstKey(),
                nonNull(order) ? order.getOrderId() : null,
                order);
        fireProducerAccordingType(operationType, producer -> producer.sendMessage(changeStreamDoc));
    }


    private void fireProducerAccordingType(OperationType operationType, Consumer<OrderEventProducer> action) {
        ofNullable(orderEventProducersByOperationType.get(operationType)).ifPresentOrElse(
                action,
                () -> log.warn("Didn't find appropriate producer for data change event ={}. So that, event was skipped.",
                        operationType));
    }
}