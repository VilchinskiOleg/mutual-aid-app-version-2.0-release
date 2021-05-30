package order.orderservice.persistent.repository;

import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.persistent.entity.Order;
import org.springframework.data.domain.Page;

public interface ExtendedOrderRepository {

    Page<Order> searchByFilters(SearchOrderDetails searchOrderDetails);
}
