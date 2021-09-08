package org.tms.profile_service_rest.rest.validation.validator;

import static java.time.LocalDate.now;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.tms.profile_service_rest.rest.validation.annotation.ValidDate.Type.FUTURE;
import static org.tms.profile_service_rest.rest.validation.annotation.ValidDate.Type.PAST;

import org.tms.profile_service_rest.rest.validation.annotation.ValidDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {

    private final Map<ValidDate.Type, Predicate<LocalDate>> predicates = new HashMap<>();
    private ValidDate.Type validationType;

    private final Predicate<LocalDate> pastDateValidator = (date) -> date.minusDays(INTEGER_ONE).isBefore(now());

    private final Predicate<LocalDate> futureDateValidator = (date) -> date.plusDays(INTEGER_ONE).isAfter(now());

    @Override
    public void initialize(ValidDate annotation) {
        predicates.put(PAST, pastDateValidator);
        predicates.put(FUTURE, futureDateValidator);
        validationType = annotation.type();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (isNull(date)) {
            return false;
        }
        return predicates.get(validationType).test(date);
    }
}
