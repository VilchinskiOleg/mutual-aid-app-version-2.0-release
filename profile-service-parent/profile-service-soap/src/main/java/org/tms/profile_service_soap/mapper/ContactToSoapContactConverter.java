package org.tms.profile_service_soap.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Contact;

@Component
public class ContactToSoapContactConverter extends BaseConverter<Contact, org.tms.profile_service_soap.endpoint.model.Contact> {

    @Override
    protected org.tms.profile_service_soap.endpoint.model.Contact getDestination() {
        return new org.tms.profile_service_soap.endpoint.model.Contact();
    }

    @Override
    public void convert(Contact source, org.tms.profile_service_soap.endpoint.model.Contact destination) {
        destination.setType(mapper.map(source.getType()));
        destination.setValue(source.getValue());
    }
}