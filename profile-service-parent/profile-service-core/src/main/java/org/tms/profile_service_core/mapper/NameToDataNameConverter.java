package org.tms.profile_service_core.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Name;

@Component
public class NameToDataNameConverter extends BaseConverter<Name, org.tms.profile_service_core.persistent.entity.Name> {

    @Override
    protected org.tms.profile_service_core.persistent.entity.Name getDestination() {
        return new org.tms.profile_service_core.persistent.entity.Name();
    }

    @Override
    public void convert(Name source, org.tms.profile_service_core.persistent.entity.Name destination) {
        destination.setLocale(source.getLocale());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}