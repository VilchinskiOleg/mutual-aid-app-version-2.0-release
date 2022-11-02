package order.orderservice.domain.service.strategy;

import static java.time.LocalDateTime.now;
import static order.orderservice.domain.model.Order.Status.AWAITING_APPROVAL;
import static order.orderservice.domain.model.Order.Status.IN_WORK;
import static order.orderservice.util.Constant.Errors.CANNOT_APPROVE_EXECUTION;

import order.orderservice.domain.model.Order;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ApproveOrderStrategy extends AbstractManageOrderStateStrategy {

  @Override
  protected void checkCurrentState(Order order) {
    var currentStatus = order.getStatus();
    if (currentStatus != AWAITING_APPROVAL) {
      throw new ConflictException(CANNOT_APPROVE_EXECUTION);
    }
  }

  @Override
  @PreAuthorize("hasRole('APPROVE_ORDER_AS_OWNER') or #order.owner.memberId == authentication.profileId")
  protected void updateOrderState(Order order, String... args) {
    profileService.populateOrderExecutor(order, args[0]);
    order.setStatus(IN_WORK);
    order.setModifyAt(now());
  }
}