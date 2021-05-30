package order.orderservice.util.validation.annotation;

import order.orderservice.util.validation.validator.OrderValidator;
import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = OrderValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrder {

    String message() default "";
}
