package org.tms.profile_service_rest.rest.validation.annotation;

import org.tms.profile_service_rest.rest.validation.validator.DateValidator;
import org.tms.profile_service_rest.rest.validation.validator.NamesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = NamesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNames {

    String message() default "";

    /**
     * It's required methods for custom constraint annotation:
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
