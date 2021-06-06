package order.orderservice.mapper;

import static java.util.Objects.nonNull;

import order.orderservice.domain.model.Order;
import order.orderservice.persistent.entity.Location;
import order.orderservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
public class OrderToDataOrderConverter extends BaseConverter<Order, order.orderservice.persistent.entity.Order> {

    @Override
    protected order.orderservice.persistent.entity.Order getDestination() {
        return new order.orderservice.persistent.entity.Order();
    }

    @Override
    public void convert(Order source, order.orderservice.persistent.entity.Order destination) {
        destination.setOrderId(source.getOrderId());
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setLocation(mapper.map(source.getLocation(), Location.class));
        destination.setPrice(source.getPrice());

        destination.setType(mapper.map(source.getType()));
        destination.setStatus(mapper.map(source.getStatus()));
        if (nonNull(source.getPriority())) {
            destination.setPriority(mapper.map(source.getPriority()));
        }

        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());

        destination.setOwner(mapper.map(source.getOwner(), Member.class));
        destination.setCandidates(mapper.map(source.getCandidates(), new HashSet<>(), Member.class));
        if (nonNull(source.getExecutor())) {
            destination.setExecutor(mapper.map(source.getExecutor(), Member.class));
        }
    }
}
