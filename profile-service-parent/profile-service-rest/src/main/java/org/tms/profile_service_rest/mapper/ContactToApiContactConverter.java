package org.tms.profile_service_rest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Contact;

@Component
public class ContactToApiContactConverter extends BaseConverter<Contact, org.tms.profile_service_rest.rest.model.Contact> {

    @Override
    protected org.tms.profile_service_rest.rest.model.Contact getDestination() {
        return new org.tms.profile_service_rest.rest.model.Contact();
    }

    @Override
    public void convert(Contact source, org.tms.profile_service_rest.rest.model.Contact destination) {
        destination.setType(mapper.map(source.getType()));
        destination.setValue(source.getValue());
    }
}
