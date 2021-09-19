package org.tms.profile_service_rest.rest.validation.annotation;

import org.tms.profile_service_rest.rest.validation.validator.ContactsValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ReportAsSingleViolation
@Constraint(validatedBy = ContactsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContacts {

    String message() default "";

    /**
     * It's required methods for custom constraint annotation:
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
