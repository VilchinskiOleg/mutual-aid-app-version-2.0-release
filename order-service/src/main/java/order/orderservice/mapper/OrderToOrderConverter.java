package order.orderservice.mapper;

import static java.util.Objects.nonNull;

import order.orderservice.domain.model.Location;
import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderToOrderConverter extends BaseConverter<Order, Order> {

    @Override
    protected Order getDestination() {
        return new Order();
    }

    @Override
    public void convert(Order source, Order destination) {
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setLocation(mapper.map(source.getLocation(), Location.class));
        if (nonNull(source.getPrice())) {
            destination.setPrice(source.getPrice());
        }
        destination.setType(source.getType());
        if (nonNull(source.getPriority())) {
            destination.setPriority(mapper.map(source.getPriority(), Order.Priority.class));
        }
        destination.setOwner(mapper.map(source.getOwner(), Member.class));
    }
}
