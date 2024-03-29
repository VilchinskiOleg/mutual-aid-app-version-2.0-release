package order.orderservice.mapper;

import order.orderservice.persistent.mongo.entity.Location;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataLocationToLocationConverter extends BaseConverter<Location, order.orderservice.domain.model.Location> {

    @Override
    protected order.orderservice.domain.model.Location getDestination() {
        return new order.orderservice.domain.model.Location();
    }

    @Override
    public void convert(Location source, order.orderservice.domain.model.Location destination) {
        destination.setCountry(source.getCountry());
        destination.setCity(source.getCity());
        destination.setStreet(source.getStreet());
        destination.setHome(source.getHome());
        destination.setFlat(source.getFlat());
    }
}
