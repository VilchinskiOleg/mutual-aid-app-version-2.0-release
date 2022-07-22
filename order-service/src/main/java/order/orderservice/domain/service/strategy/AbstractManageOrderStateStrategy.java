package order.orderservice.domain.service.strategy;

import javax.annotation.Resource;
import order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.service.OrderService;
import order.orderservice.domain.service.processor.EventManagerService;
import order.orderservice.domain.service.processor.ProfileService;

public abstract class AbstractManageOrderStateStrategy implements ManageOrderStateStrategy {

  @Resource
  protected EventManagerService eventManagerService;
  @Resource
  protected ProfileService profileService;
  @Resource
  private OrderService orderService;


  @Override
  public Order manageOrder(String orderId, String ... args) {
    Order currentOrder = orderService.findByOrderIdRequired(orderId);
    checkCurrentState(currentOrder);
    updateOrderState(currentOrder, args);
    Order savedOrder = orderService.saveOrder(currentOrder);
    eventManagerService.sendEvent(OperationType.UPDATE, savedOrder);
    return savedOrder;
  }

  protected abstract void checkCurrentState(Order order);

  protected abstract void updateOrderState(Order order, String ... args);
}