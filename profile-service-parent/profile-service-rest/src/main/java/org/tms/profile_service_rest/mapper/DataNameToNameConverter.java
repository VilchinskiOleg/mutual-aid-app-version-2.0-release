package org.tms.profile_service_rest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.persistent.entity.Name;

@Component
public class DataNameToNameConverter extends BaseConverter<Name, org.tms.profile_service_rest.domain.model.Name> {

    @Override
    protected org.tms.profile_service_rest.domain.model.Name getDestination() {
        return new org.tms.profile_service_rest.domain.model.Name();
    }

    @Override
    public void convert(Name source, org.tms.profile_service_rest.domain.model.Name destination) {
        destination.setLocale(source.getLocale());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}
