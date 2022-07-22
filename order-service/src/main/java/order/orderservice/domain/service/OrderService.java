package order.orderservice.domain.service;

import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;

import java.util.List;

public interface OrderService {

    Order createOrder(Order orderDetails);

    Order updateOrder(Order orderDetails, String orderId);

    Order findByOrderIdRequired(String orderId);

    Page<Order> findByFilters(SearchOrderDetails searchOrderDetails);

    Page<Order> findByPartOfTitle(String subTitle, Integer pageNumber, Integer size);

    List<Order> findByExecutorOrCandidateIds(String memberId);

    List<Order> findByOwnerId(String memberId);

    Integer removeOrdersAsync(List<Order> orders);

    Order saveOrder(Order order);
}