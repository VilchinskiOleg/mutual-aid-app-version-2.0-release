package order.orderservice.mapper;

import order.orderservice.domain.model.Location;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class LocationToDataLocationConverter extends BaseConverter<Location, order.orderservice.persistent.entity.Location> {

    @Override
    protected order.orderservice.persistent.entity.Location getDestination() {
        return new order.orderservice.persistent.entity.Location();
    }

    @Override
    public void convert(Location source, order.orderservice.persistent.entity.Location destination) {
        destination.setCountry(source.getCountry());
        destination.setCity(source.getCity());
        destination.setStreet(source.getStreet());
        destination.setHome(source.getHome());
        destination.setFlat(source.getFlat());
    }
}
