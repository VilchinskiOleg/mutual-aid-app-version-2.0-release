package order.orderservice.rest.message;

import lombok.Getter;
import lombok.Setter;
import order.orderservice.rest.model.Order;
import java.util.List;

@Getter
@Setter
public class OrdersResponse {

    private List<Order> orders;
    private Integer allPages;
    private Integer currentPage;
    private Integer sizeOfPage;
}
