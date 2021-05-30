package order.orderservice.util.validation.validator;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Type.PAID;
import static order.orderservice.domain.model.Order.Type.UNPAID;
import static order.orderservice.domain.model.Order.Priority.valueOf;

import order.orderservice.rest.model.Order;
import order.orderservice.util.validation.annotation.ValidOrder;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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
        try {
            valueOf(order.getPriority());
        } catch (IllegalArgumentException ex) {
            isValid = false;
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
    public boolean isValid(Order order, ConstraintValidatorContext constraintValidatorContext) {
        var rule = predicates.get(order.getType());
        return rule.test(order);
    }
}
