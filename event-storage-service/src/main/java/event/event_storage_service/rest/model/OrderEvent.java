package event.event_storage_service.rest.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderEvent {

    private String id;

    private String operationType;
    private OrderDetails orderDetails;
    private LocalDateTime createAt;
    private String createBy;
}
