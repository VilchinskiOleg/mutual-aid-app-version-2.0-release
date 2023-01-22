package org.tms.profile_service_core.mapper.client;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Name;

@Component
public class NameToAuthNameConverter extends BaseConverter<Name, org.tms.mutual_aid.auth.client.model.Name> {

    @Override
    public void convert(Name source, org.tms.mutual_aid.auth.client.model.Name destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setLocale(source.getLocale().getLanguage());
    }
}