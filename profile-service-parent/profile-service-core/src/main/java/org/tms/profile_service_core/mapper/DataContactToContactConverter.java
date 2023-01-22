package org.tms.profile_service_core.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.persistent.entity.Contact;

import static org.tms.profile_service_core.domain.model.Contact.Type;

@Component
public class DataContactToContactConverter extends BaseConverter<Contact, org.tms.profile_service_core.domain.model.Contact> {

    @Override
    protected org.tms.profile_service_core.domain.model.Contact getDestination() {
        return new org.tms.profile_service_core.domain.model.Contact();
    }

    @Override
    public void convert(Contact source, org.tms.profile_service_core.domain.model.Contact destination) {
        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setValue(source.getValue());
    }
}