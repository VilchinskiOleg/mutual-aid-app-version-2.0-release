package order.orderservice.domain.service.strategy;

import order.orderservice.domain.model.Order;

public interface ManageOrderStateStrategy {

  Order manageOrder(String orderId, String ... args);
}