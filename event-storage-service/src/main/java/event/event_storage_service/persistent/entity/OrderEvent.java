package event.event_storage_service.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "event-storage-service.order_event")
public class OrderEvent {

    @Id
    private String id;

    private String operationType;
    private OrderDetails orderDetails;
    private LocalDateTime createAt;
    private String createBy;
}
