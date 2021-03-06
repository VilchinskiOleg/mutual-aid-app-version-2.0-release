package order.orderservice.rest.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import order.orderservice.rest.model.Order;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponse extends BaseResponse {

    private List<Order> orders;
}
