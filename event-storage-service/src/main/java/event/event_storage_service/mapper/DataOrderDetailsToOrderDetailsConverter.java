package event.event_storage_service.mapper;

import event.event_storage_service.persistent.entity.OrderDetails;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataOrderDetailsToOrderDetailsConverter extends BaseConverter<OrderDetails, event.event_storage_service.domain.model.OrderDetails> {

    @Override
    protected event.event_storage_service.domain.model.OrderDetails getDestination() {
        return new event.event_storage_service.domain.model.OrderDetails();
    }

    @Override
    public void convert(OrderDetails source, event.event_storage_service.domain.model.OrderDetails destination) {
        destination.setOrderId(source.getOrderId());
        destination.setStatus(source.getStatus());
    }
}
