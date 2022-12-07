package org.tms.profile_service_rest.mapper.client;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Contact;

@Component
public class ContactToAuthContactConverter extends BaseConverter<Contact, org.tms.mutual_aid.auth.client.model.Contact> {

    @Override
    public void convert(Contact source, org.tms.mutual_aid.auth.client.model.Contact destination) {
        destination.setType(mapper.map(source.getType()));
        destination.setValue(source.getValue());
    }
}