package org.tms.profile_service_rest.rest.validation.annotation;

import org.tms.profile_service_rest.rest.validation.validator.DateValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = DateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {

    String message() default "";

    /**
     * It's required methods for custom constraint annotation:
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * It's custom methods:
     */

    Type type();

    enum Type {
        FUTURE,
        PAST
    }
}
