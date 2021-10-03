package order.orderservice.rest.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import order.orderservice.rest.model.Order;
import org.exception.handling.autoconfiguration.model.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends BaseResponse {

    private Order order;
}
