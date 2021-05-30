package order.orderservice.rest.model;

import static order.orderservice.util.Constant.Errors.*;

import lombok.Getter;
import lombok.Setter;
import order.orderservice.util.validation.annotation.ValidOrder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ValidOrder(message = ORDER_RULE_BY_TYPE)
public class Order {

    private String orderId;
    @NotBlank(message = ORDER_TITLE_REQUIRED)
    private String title;
    private String description;
    @NotNull(message = ORDER_LOCATION_REQUIRED)
    private Location location;
    private BigDecimal price;

    @Pattern(regexp = "^PAID$", message = ORDER_TYPE_RULE)
    private String type;
    private String status;
    private String priority;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    @NotNull(message = ORDER_OWNER_REQUIRED)
    private Member owner;
    private Set<Member> candidates;
    private Member executor;
}
