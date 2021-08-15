package event.event_storage_service.domain.service;

import static event.event_storage_service.util.Constant.Errors.EVENT_INCORRECT_ORDER_ID;
import static org.apache.logging.log4j.util.Strings.isBlank;

import event.event_storage_service.domain.model.OrderEvent;
import event.event_storage_service.persistent.repository.OrderEventRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventStorageServiceImpl implements EventStorageService {

    @Resource
    private OrderEventRepository orderEventRepository;
    @Resource
    private Mapper mapper;

    @Override
    public OrderEvent saveEvent(OrderEvent orderEvent) {
        var dataOrderEvent = orderEventRepository.save(mapper.map(orderEvent, event.event_storage_service.persistent.entity.OrderEvent.class));
        return mapper.map(dataOrderEvent, OrderEvent.class);
    }

    @Override
    public List<OrderEvent> getEventsByOrderId(String orderId) {
        checkOrderId(orderId);
        var dataOrderEvents = orderEventRepository.findByOrderDetails_OrderId(orderId);
        return mapper.map(dataOrderEvents, new ArrayList<>(), OrderEvent.class);
    }

    private void checkOrderId(String orderId) {
        if (isBlank(orderId)) {
            throw new ConflictException(EVENT_INCORRECT_ORDER_ID);
        }
    }
}
