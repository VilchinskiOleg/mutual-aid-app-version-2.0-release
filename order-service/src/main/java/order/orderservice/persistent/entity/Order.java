package order.orderservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Document(collection = "order-service.order")
public class Order {

    @Id
    private String id;

    private String orderId;
    private String title;
    private String description;
    private Location location;
    private BigDecimal price;

    private String type;
    private String status;
    private String priority;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    private Member owner;
    private Set<Member> candidates;
    private Member executor;
}
