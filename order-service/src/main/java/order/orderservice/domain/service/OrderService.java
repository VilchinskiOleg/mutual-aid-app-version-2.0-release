package order.orderservice.domain.service;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;

public interface OrderService {

    Order createOrder(Order orderDetails);

    Order findByOrderId(String orderId);
    Page<Order> findByFilters(SearchOrderDetails searchOrderDetails);
}
