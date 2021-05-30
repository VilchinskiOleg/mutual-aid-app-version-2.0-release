package order.orderservice.rest.message;

import static order.orderservice.util.Constant.Errors.SEARCH_ORDER_DETAILS_PAGING_RULE;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class SearchOrderDetails {

    @NotNull(message = SEARCH_ORDER_DETAILS_PAGING_RULE)
    private Integer numberOfPage;
    @NotNull(message = SEARCH_ORDER_DETAILS_PAGING_RULE)
    private Integer sizeOfPage;

    private BigDecimal price;
    private String type;
    private String priority;

    private String locationCountry;
    private String locationCity;
    private String locationStreet;
    private String locationHome;
}
