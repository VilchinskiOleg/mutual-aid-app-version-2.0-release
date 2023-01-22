package org.tms.profile_service_soap.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Name;

@Component
public class NameToSoapNameConverter extends BaseConverter<Name, org.tms.profile_service_soap.endpoint.model.Name> {

    @Override
    protected org.tms.profile_service_soap.endpoint.model.Name getDestination() {
        return new org.tms.profile_service_soap.endpoint.model.Name();
    }

    @Override
    public void convert(Name source, org.tms.profile_service_soap.endpoint.model.Name destination) {
        destination.setLocale(source.getLocale().getLanguage());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}