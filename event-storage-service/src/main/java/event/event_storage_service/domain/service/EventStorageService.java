package event.event_storage_service.domain.service;

import event.event_storage_service.domain.model.OrderEvent;
import java.util.List;

public interface EventStorageService {

    OrderEvent saveEvent(OrderEvent orderEvent);

    List<OrderEvent> getEventsByOrderId(String orderId);
}
