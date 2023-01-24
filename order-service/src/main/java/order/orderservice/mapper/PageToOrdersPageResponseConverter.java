package order.orderservice.mapper;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.rest.message.OrdersPageResponse;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class PageToOrdersPageResponseConverter extends BaseConverter<Page<Order>, OrdersPageResponse> {

    @Override
    protected OrdersPageResponse getDestination() {
        return new OrdersPageResponse();
    }

    @Override
    public void convert(Page<Order> source, OrdersPageResponse destination) {
        destination.setOrders(mapper.map(source.getPayload(), new ArrayList<>(), order.orderservice.rest.model.Order.class));
        destination.setCurrentPage(source.getCurrentPage());
        destination.setAllPages(source.getAllPages());
        destination.setSizeOfPage(source.getSizeOfPage());
    }
}