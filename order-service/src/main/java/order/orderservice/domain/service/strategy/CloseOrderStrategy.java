package order.orderservice.domain.service.strategy;

import static java.time.LocalDateTime.now;
import static order.orderservice.domain.model.Order.Status.CLOSED;
import static order.orderservice.domain.model.Order.Status.IN_WORK;
import static order.orderservice.util.Constant.Errors.CANNOT_CLOSE_ORDER;

import order.orderservice.domain.model.Order;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class CloseOrderStrategy extends AbstractManageOrderStateStrategy {

  @Override
  protected void checkCurrentState(Order order) {
    var currentStatus = order.getStatus();
    if (currentStatus != IN_WORK) {
      throw new ConflictException(CANNOT_CLOSE_ORDER);
    }
  }

  @Override
  @PreAuthorize("hasRole('CLOSE_ORDER_AS_OWNER') or #order.owner.memberId == authentication.profileId")
  protected void updateOrderState(Order order, String... args) {
    order.setStatus(CLOSED);
    order.setModifyAt(now());
  }
}