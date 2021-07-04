package order.orderservice.rest.validation.validator;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Type.PAID;
import static order.orderservice.domain.model.Order.Type.UNPAID;
import static org.apache.logging.log4j.util.Strings.isBlank;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.rest.model.Order;
import order.orderservice.rest.validation.annotation.ValidOrder;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
public class OrderValidator implements ConstraintValidator<ValidOrder, Order> {

    private Map<String, Predicate<Order>> predicates;

    private Predicate<Order> paidOrderPredicate = order -> {
        boolean isValid = true;
        if (isNull(order.getPrice())) {
            isValid = false;
        }
        return isValid;
    };

    private Predicate<Order> unpaidOrderPredicate = order -> {
        boolean isValid = true;
        if (nonNull(order.getPrice())) {
            isValid = false;
        }
        if (isNull(order.getPriority())) {
            isValid= false;
        }
        return isValid;
    };

    @Override
    public void initialize(ValidOrder constraintAnnotation) {
        predicates = new HashMap<>();
        predicates.put(PAID.toString(), paidOrderPredicate);
        predicates.put(UNPAID.toString(), unpaidOrderPredicate);
    }

    @Override
    public boolean isValid(Order order, ConstraintValidatorContext context) {
        var orderType = order.getType();
        if (isBlank(orderType)) {
            log.error("Unexpected error: order type cannot be 'null'");
            return false;
        }
        var rule = predicates.get(orderType);
        if (isNull(rule)) {
            log.error("Unexpected error: have not validation rule for order type '{}'", orderType);
            return false;
        }
        return rule.test(order);
    }
}
