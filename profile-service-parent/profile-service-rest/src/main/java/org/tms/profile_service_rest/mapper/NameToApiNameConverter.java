package org.tms.profile_service_rest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Name;

@Component
public class NameToApiNameConverter extends BaseConverter<Name, org.tms.profile_service_rest.rest.model.Name> {

    @Override
    protected org.tms.profile_service_rest.rest.model.Name getDestination() {
        return new org.tms.profile_service_rest.rest.model.Name();
    }

    @Override
    public void convert(Name source, org.tms.profile_service_rest.rest.model.Name destination) {
        destination.setLocale(source.getLocale().getLanguage());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}
