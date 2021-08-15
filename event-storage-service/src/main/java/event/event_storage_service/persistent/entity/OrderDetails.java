package event.event_storage_service.persistent.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetails {

    private String orderId;
    private String status;
}
