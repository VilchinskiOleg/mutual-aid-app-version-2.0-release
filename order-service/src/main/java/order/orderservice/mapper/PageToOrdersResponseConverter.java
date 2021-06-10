package order.orderservice.mapper;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.rest.message.OrdersResponse;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class PageToOrdersResponseConverter extends BaseConverter<Page<Order>, OrdersResponse> {

    @Override
    protected OrdersResponse getDestination() {
        return new OrdersResponse();
    }

    @Override
    public void convert(Page<Order> source, OrdersResponse destination) {
        destination.setOrders(mapper.map(source.getPayload(), new ArrayList<>(), order.orderservice.rest.model.Order.class));
        destination.setCurrentPage(source.getCurrentPage());
        destination.setAllPages(source.getAllPages());
        destination.setSizeOfPage(source.getSizeOfPage());
    }
}

