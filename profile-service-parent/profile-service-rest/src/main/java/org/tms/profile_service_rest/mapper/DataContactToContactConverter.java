package org.tms.profile_service_rest.mapper;

import static org.tms.profile_service_rest.domain.model.Contact.Type;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.persistent.entity.Contact;

@Component
public class DataContactToContactConverter extends BaseConverter<Contact, org.tms.profile_service_rest.domain.model.Contact> {

    @Override
    protected org.tms.profile_service_rest.domain.model.Contact getDestination() {
        return new org.tms.profile_service_rest.domain.model.Contact();
    }

    @Override
    public void convert(Contact source, org.tms.profile_service_rest.domain.model.Contact destination) {
        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setValue(source.getValue());
    }
}
