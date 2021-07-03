package order.orderservice.rest.message;

import static order.orderservice.util.Constant.Errors.ORDER_PRIORITY_RULE;
import static order.orderservice.util.Constant.Errors.SEARCH_ORDER_DETAILS_PAGING_RULE;

import lombok.Getter;
import lombok.Setter;
import order.orderservice.rest.validation.annotation.ValidOrderPriority;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class SearchOrderDetails {

    @NotNull(message = SEARCH_ORDER_DETAILS_PAGING_RULE)
    private Integer numberOfPage;
    @NotNull(message = SEARCH_ORDER_DETAILS_PAGING_RULE)
    private Integer sizeOfPage;

    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String type;
    @ValidOrderPriority(pattern = "^(CRITICAL|NOT_CRITICAL)$", message = ORDER_PRIORITY_RULE)
    private String priority;

    private String locationCountry;
    private String locationCity;
    private String locationStreet;
    private String locationHome;
}
