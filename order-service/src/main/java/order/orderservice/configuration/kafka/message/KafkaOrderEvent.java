package order.orderservice.configuration.kafka.message;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KafkaOrderEvent implements Serializable {

    private OperationType operationType;
    private LocalDateTime createAt;
    private String createBy;

    private String orderId;
    private String status;

    public KafkaOrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public enum OperationType {
        CREATE,
        UPDATE,
        DELETE
    }
}