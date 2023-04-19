package event.event_storage_service.mapper;

import com.mongodb.client.model.changestream.OperationType;
import event.event_storage_service.domain.model.OrderDetails;
import event.event_storage_service.persistent.entity.OrderEvent;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class DataOrderEventToOrderEventConverter extends BaseConverter<OrderEvent, event.event_storage_service.domain.model.OrderEvent> {

    @Override
    protected event.event_storage_service.domain.model.OrderEvent getDestination() {
        return new event.event_storage_service.domain.model.OrderEvent();
    }

    @Override
    public void convert(OrderEvent source, event.event_storage_service.domain.model.OrderEvent destination) {
        destination.setId(source.getId());
        destination.setOperationType(mapper.map(source.getOperationType(), OperationType.class));
        if (nonNull(source.getOrderDetails())) {
            destination.setOrderDetails(mapper.map(source.getOrderDetails(), OrderDetails.class));
        }
        destination.setCreateAt(source.getCreateAt());
        destination.setCreateBy(source.getCreateBy());
    }
}