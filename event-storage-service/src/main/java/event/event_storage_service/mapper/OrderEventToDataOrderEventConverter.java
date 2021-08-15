package event.event_storage_service.mapper;

import static java.util.Objects.nonNull;

import event.event_storage_service.domain.model.OrderEvent;
import event.event_storage_service.persistent.entity.OrderDetails;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToDataOrderEventConverter extends BaseConverter<OrderEvent, event.event_storage_service.persistent.entity.OrderEvent> {

    @Override
    protected event.event_storage_service.persistent.entity.OrderEvent getDestination() {
        return new event.event_storage_service.persistent.entity.OrderEvent();
    }

    @Override
    public void convert(OrderEvent source, event.event_storage_service.persistent.entity.OrderEvent destination) {
        destination.setOperationType(mapper.map(source.getOperationType()));
        if (nonNull(source.getOrderDetails())) {
            destination.setOrderDetails(mapper.map(source.getOrderDetails(), OrderDetails.class));
        }
        destination.setCreateAt(source.getCreateAt());
        destination.setCreateBy(source.getCreateBy());
    }
}
