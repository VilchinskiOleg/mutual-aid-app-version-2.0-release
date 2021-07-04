package order.orderservice.rest.validation.annotation;

import order.orderservice.rest.validation.validator.OrderTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = OrderTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderType {

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
