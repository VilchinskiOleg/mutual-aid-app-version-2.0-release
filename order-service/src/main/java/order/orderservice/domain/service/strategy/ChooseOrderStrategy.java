package order.orderservice.domain.service.strategy;

import static java.time.LocalDateTime.now;
import static order.orderservice.domain.model.Order.Status.ACTIVE;
import static order.orderservice.domain.model.Order.Status.AWAITING_APPROVAL;
import static order.orderservice.domain.model.Order.Status.CLOSED;
import static order.orderservice.domain.model.Order.Status.IN_WORK;
import static order.orderservice.util.Constant.Errors.CANNOT_ADD_NEW_CANDIDATE;

import order.orderservice.domain.model.Order;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;

@Component
public class ChooseOrderStrategy extends AbstractManageOrderStateStrategy {

  @Override
  protected void updateOrderState(Order order, String... args) {
    profileService.populateOrderExecutionCandidate(order, args[0]);
    order.setModifyAt(now());
    if (order.getStatus() == ACTIVE) {
      order.setStatus(AWAITING_APPROVAL);
    }
  }

  @Override
  protected void checkCurrentState(Order order) {
    var currentStatus = order.getStatus();
    if (currentStatus == IN_WORK || currentStatus == CLOSED) {
      throw new ConflictException(CANNOT_ADD_NEW_CANDIDATE);
    }
  }
}