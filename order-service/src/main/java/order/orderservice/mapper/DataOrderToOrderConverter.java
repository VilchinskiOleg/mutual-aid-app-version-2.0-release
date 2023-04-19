package order.orderservice.mapper;

import order.orderservice.domain.model.Location;
import order.orderservice.domain.model.Member;
import order.orderservice.persistent.mongo.entity.Order;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.*;

@Component
public class DataOrderToOrderConverter extends BaseConverter<Order, order.orderservice.domain.model.Order> {

    @Override
    protected order.orderservice.domain.model.Order getDestination() {
        return new order.orderservice.domain.model.Order();
    }

    @Override
    public void convert(Order source, order.orderservice.domain.model.Order destination) {
        destination.setId(source.getId().toHexString());
        destination.setOrderId(source.getOrderId());
        destination.setTitle(source.getTitle());
        destination.setDescription(source.getDescription());
        destination.setLocation(mapper.map(source.getLocation(), Location.class));
        destination.setPrice(source.getPrice());

        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setStatus(mapper.map(source.getStatus(), Status.class));
        if (nonNull(source.getPriority())) {
            destination.setPriority(mapper.map(source.getPriority(), Priority.class));
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
