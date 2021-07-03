package order.orderservice.mapper;

import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Type.PAID;
import static order.orderservice.util.Constant.ModelMapper.UPDATE;

import order.orderservice.domain.model.Location;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
@ModelMapperContext(value = UPDATE)
public class OrderToOrderUpdateConverter extends BaseConverter<Order, Order> {

    @Override
    protected Order getDestination() {
        return new Order();
    }

    @Override
    public void convert(Order source, Order destination) {
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setLocation(mapper.map(source.getLocation(), Location.class));
        if (nonNull(source.getPrice()) && destination.getType() == PAID) {
            destination.setPrice(source.getPrice());
        }
        destination.setPriority(source.getPriority());
    }
}
