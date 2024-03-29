package org.tms.profile_service_core.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Contact;
import org.tms.profile_service_core.domain.model.Name;
import org.tms.profile_service_core.persistent.entity.Profile;

import java.util.ArrayList;

import static org.tms.profile_service_core.domain.model.Profile.Gender;

@Component
public class DataProfileToProfileConverter extends BaseConverter<Profile, org.tms.profile_service_core.domain.model.Profile> {

    @Override
    protected org.tms.profile_service_core.domain.model.Profile getDestination() {
        return new org.tms.profile_service_core.domain.model.Profile();
    }

    @Override
    public void convert(Profile source, org.tms.profile_service_core.domain.model.Profile destination) {
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setBirthday(source.getBirthday());
        destination.setGender(mapper.map(source.getGender(), Gender.class));

        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setProfileId(source.getProfileId());
        destination.setId(source.getId());
    }
}