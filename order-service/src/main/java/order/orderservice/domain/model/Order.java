package order.orderservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class Order {

    private String id;

    private String orderId;
    private String title;
    private String description;
    private Location location;
    private BigDecimal price;

    private Type type;
    private Status status;
    private Priority priority;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    private Member owner;
    private Set<Member> candidates;
    private Member executor;

    public enum Type {
        PAID,
        UNPAID
    }

    public enum Status {
        ACTIVE,
        AWAITING_APPROVAL,
        IN_WORK,
        CLOSED
    }

    public enum Priority {
        NOT_CRITICAL,
        CRITICAL
    }
}
