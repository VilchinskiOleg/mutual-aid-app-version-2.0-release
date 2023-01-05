package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.profile.Contact.Type;
import org.tms.authservicerest.rest.model.profile.Contact;

@Component
public class ApiContactToContactConverter extends BaseConverter<Contact, org.tms.authservicerest.domain.model.profile.Contact> {

    @Override
    public void convert(Contact source, org.tms.authservicerest.domain.model.profile.Contact destination) {
        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setValue(source.getValue());
    }
}