package event.event_storage_service.mapper.kafka;

import static event.event_storage_service.domain.model.OrderEvent.OperationType;

import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.model.OrderDetails;
import event.event_storage_service.domain.model.OrderEvent;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventToOrderEventConverter extends BaseConverter<KafkaOrderEvent, OrderEvent> {

    @Override
    protected OrderEvent getDestination() {
        return new OrderEvent();
    }

    @Override
    public void convert(KafkaOrderEvent source, OrderEvent destination) {
        destination.setOperationType(mapper.map(source.getOperationType().name(), OperationType.class));
        destination.setCreateAt(source.getCreateAt());
        destination.setCreateBy(source.getCreateBy());
        destination.setOrderDetails(buildOrderDetails(source));
    }

    private OrderDetails buildOrderDetails(KafkaOrderEvent kafkaOrderEvent) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(kafkaOrderEvent.getOrderId());
        orderDetails.setStatus(kafkaOrderEvent.getStatus());
        return orderDetails;
    }
}
