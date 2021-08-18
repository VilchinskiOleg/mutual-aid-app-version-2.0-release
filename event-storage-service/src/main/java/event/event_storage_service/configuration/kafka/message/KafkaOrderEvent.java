package event.event_storage_service.configuration.kafka.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class KafkaOrderEvent implements Serializable {

    private String operationType;
    private LocalDateTime createAt;
    private String createBy;

    private String orderId;
    private String status;
}
