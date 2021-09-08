package org.tms.profile_service_rest.mapper;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.tms.profile_service_rest.domain.model.Contact.Type;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.rest.model.Contact;

@Component
public class ApiContactToContactConverter extends BaseConverter<Contact, org.tms.profile_service_rest.domain.model.Contact> {

    @Override
    protected org.tms.profile_service_rest.domain.model.Contact getDestination() {
        return new org.tms.profile_service_rest.domain.model.Contact();
    }

    @Override
    public void convert(Contact source, org.tms.profile_service_rest.domain.model.Contact destination) {
        destination.setType(mapper.map(upperCase(source.getType()), Type.class));
        destination.setValue(source.getValue());
    }
}
