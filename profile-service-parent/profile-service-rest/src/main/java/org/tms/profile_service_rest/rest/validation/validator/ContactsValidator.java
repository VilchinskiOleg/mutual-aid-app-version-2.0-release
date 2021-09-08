package org.tms.profile_service_rest.rest.validation.validator;

import static org.springframework.util.CollectionUtils.isEmpty;

import org.tms.profile_service_rest.rest.model.Contact;
import org.tms.profile_service_rest.rest.validation.annotation.ValidContacts;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ContactsValidator implements ConstraintValidator<ValidContacts, List<Contact>> {

    @Override
    public boolean isValid(List<Contact> contacts, ConstraintValidatorContext context) {
        return  !isEmpty(contacts);
    }
}
