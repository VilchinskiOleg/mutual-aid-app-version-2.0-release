package event.event_storage_service.rest.message;

import event.event_storage_service.rest.model.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventsResponse {

    private List<OrderEvent> orderEvents;
}
