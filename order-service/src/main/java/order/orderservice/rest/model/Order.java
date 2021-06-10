package order.orderservice.rest.model;

import static order.orderservice.util.Constant.Errors.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import order.orderservice.util.validation.annotation.ValidOrder;
import order.orderservice.util.validation.annotation.ValidOrderPriority;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    private String type;
    private String status;
    @ValidOrderPriority(pattern = "^(CRITICAL|NOT_CRITICAL)$", message = ORDER_PRIORITY_RULE)
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyAt;

    @NotNull(message = ORDER_OWNER_REQUIRED)
    private Member owner;
    private Set<Member> candidates;
    private Member executor;
}
