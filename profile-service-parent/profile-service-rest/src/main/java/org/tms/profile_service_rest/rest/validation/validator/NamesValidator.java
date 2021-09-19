package org.tms.profile_service_rest.rest.validation.validator;

import static java.util.Locale.ENGLISH;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.springframework.util.CollectionUtils.isEmpty;

import org.tms.profile_service_rest.rest.model.Name;
import org.tms.profile_service_rest.rest.validation.annotation.ValidNames;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NamesValidator implements ConstraintValidator<ValidNames, List<Name>> {

    @Override
    public boolean isValid(List<Name> names, ConstraintValidatorContext context) {
        if (isEmpty(names)) {
            return false;
        }
        return names.stream()
                .anyMatch(name -> lowerCase(name.getLocale()).equals(ENGLISH.getLanguage()));
    }
}
