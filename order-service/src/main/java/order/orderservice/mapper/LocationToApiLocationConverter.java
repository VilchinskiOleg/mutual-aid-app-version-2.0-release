package order.orderservice.mapper;

import order.orderservice.domain.model.Location;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class LocationToApiLocationConverter extends BaseConverter<Location, order.orderservice.rest.model.Location> {

    @Override
    protected order.orderservice.rest.model.Location getDestination() {
        return new order.orderservice.rest.model.Location();
    }

    @Override
    public void convert(Location source, order.orderservice.rest.model.Location destination) {
        destination.setCountry(source.getCountry());
        destination.setCity(source.getCity());
        destination.setStreet(source.getStreet());
        destination.setHome(source.getHome());
        destination.setFlat(source.getFlat());
    }
}
