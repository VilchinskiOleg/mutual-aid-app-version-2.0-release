package order.orderservice.domain.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    public Location getLocation() {
        if (isNull(location)) {
            location = new Location();
        }
        return location;
    }

    public Set<Member> getCandidates() {
        if (isNull(candidates)) {
            candidates = new HashSet<>();
        }
        return candidates;
    }

    public Member getExecutor() {
        if (isNull(executor)) {
            executor = new Member();
        }
        return executor;
    }

    public void addCandidate(Member candidate) {
        if (nonNull(candidate)) {
            getCandidates().add(candidate);
        }
    }
}
