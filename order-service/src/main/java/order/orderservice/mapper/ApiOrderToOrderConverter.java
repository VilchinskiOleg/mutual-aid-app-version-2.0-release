package order.orderservice.mapper;

import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Type;
import static order.orderservice.domain.model.Order.Priority;

import order.orderservice.domain.model.Location;
import order.orderservice.domain.model.Member;
import order.orderservice.rest.model.Order;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ApiOrderToOrderConverter extends BaseConverter<Order, order.orderservice.domain.model.Order> {

    @Override
    protected order.orderservice.domain.model.Order getDestination() {
        return new order.orderservice.domain.model.Order();
    }

    @Override
    public void convert(Order source, order.orderservice.domain.model.Order destination) {
        destination.setOrderId(source.getOrderId());
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setLocation(mapper.map(source.getLocation(), Location.class));
        if (nonNull(source.getPrice())) {
            destination.setPrice(source.getPrice());
        }
        destination.setType(mapper.map(source.getType(), Type.class));
        if (nonNull(source.getPriority())) {
            destination.setPriority(mapper.map(source.getPriority(), Priority.class));
        }
        destination.setOwner(mapper.map(source.getOwner(), Member.class));
        if (nonNull(source.getExecutor())) {
            destination.setExecutor(mapper.map(source.getExecutor(), Member.class));
        }
    }
}
