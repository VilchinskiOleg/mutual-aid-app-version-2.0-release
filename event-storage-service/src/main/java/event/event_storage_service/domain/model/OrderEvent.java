package event.event_storage_service.domain.model;

import com.mongodb.client.model.changestream.OperationType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderEvent {

    private String id;
    private OperationType operationType;
    private OrderDetails orderDetails;
    private LocalDateTime createAt;
    private String createBy;
}