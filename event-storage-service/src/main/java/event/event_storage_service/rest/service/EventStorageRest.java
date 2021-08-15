package event.event_storage_service.rest.service;

import static org.springframework.http.HttpStatus.OK;

import event.event_storage_service.domain.service.EventStorageService;
import event.event_storage_service.rest.message.OrderEventsResponse;
import event.event_storage_service.rest.model.OrderEvent;
import io.swagger.annotations.ApiOperation;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/event-storage-service")
public class EventStorageRest {

    @Resource
    private EventStorageService eventStorageService;
    @Resource
    private Mapper mapper;

    @Api
    @ApiOperation(value = "${event-storage.operation.get-events-by-order-id}")
    @ResponseStatus(OK)
    @GetMapping("/{order-id}")
    public OrderEventsResponse getOrderEventsByOrderId(@PathVariable("order-id") String orderId) {
        var orderEvents= eventStorageService.getEventsByOrderId(orderId);
        return new OrderEventsResponse(mapper.map(orderEvents, new ArrayList<>(), OrderEvent.class));
    }
}
