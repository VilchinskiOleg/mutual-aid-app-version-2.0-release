package order.orderservice.configuration.kafka.message;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class KafkaOrderEvent implements Serializable {

    private String operationType;
    private LocalDateTime createAt;
    private String createBy;

    private String orderId;
    private String status;
}
