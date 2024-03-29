package org.tms.profile_service_rest.mapper;

import static org.apache.commons.lang3.StringUtils.lowerCase;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.rest.model.Name;
import java.util.Locale;

@Component
public class ApiNameToNameConverter extends BaseConverter<Name, org.tms.profile_service_core.domain.model.Name> {

    @Override
    protected org.tms.profile_service_core.domain.model.Name getDestination() {
        return new org.tms.profile_service_core.domain.model.Name();
    }

    @Override
    public void convert(Name source, org.tms.profile_service_core.domain.model.Name destination) {
        destination.setLocale(new Locale(lowerCase(source.getLocale())));
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
    }
}