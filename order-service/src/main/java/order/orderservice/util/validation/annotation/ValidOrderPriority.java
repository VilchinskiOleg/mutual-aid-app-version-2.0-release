package order.orderservice.util.validation.annotation;

import order.orderservice.util.validation.validator.OrderPriorityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = OrderPriorityValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderPriority {

    String message() default "";

    /**
     * It's required methods for custom constraint annotation:
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * It's custom methods:
     */

    String pattern() default "^[A-Z\\\\w]{3,10}$";
}
