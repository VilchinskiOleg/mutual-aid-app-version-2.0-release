package event.event_storage_service.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetails {

    private String orderId;
    private String status;
}