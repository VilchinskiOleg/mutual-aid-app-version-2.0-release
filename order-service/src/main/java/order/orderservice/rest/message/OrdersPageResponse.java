package order.orderservice.rest.message;

import lombok.Getter;
import lombok.Setter;
import order.orderservice.rest.model.Order;
import org.exception.handling.autoconfiguration.model.BaseResponse;

import java.util.List;

@Getter
@Setter
public class OrdersPageResponse extends BaseResponse {

    private List<Order> orders;
    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
