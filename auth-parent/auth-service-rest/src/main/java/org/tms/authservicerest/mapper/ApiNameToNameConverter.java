package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.rest.model.profile.Name;
import java.util.Locale;

@Component
public class ApiNameToNameConverter extends BaseConverter<Name, org.tms.authservicerest.domain.model.profile.Name> {

    @Override
    public void convert(Name source, org.tms.authservicerest.domain.model.profile.Name destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setLocale(new Locale(source.getLocale()));
    }
}