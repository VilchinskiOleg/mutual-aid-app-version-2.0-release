package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.profile.Contact;
import org.tms.authservicerest.domain.model.profile.Name;
import org.tms.authservicerest.rest.model.Profile;
import org.tms.authservicerest.domain.model.Profile.Gender;
import java.util.ArrayList;

@Component
public class ApiProfileToProfileConverter extends BaseConverter<Profile, org.tms.authservicerest.domain.model.Profile> {

    @Override
    public void convert(Profile source, org.tms.authservicerest.domain.model.Profile destination) {
        destination.setResourceId(source.getResourceId());
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setGender(mapper.map(source.getGender(), Gender.class));
        destination.setBirthday(source.getBirthday());
        destination.setPassword(source.getPassword());
    }
}