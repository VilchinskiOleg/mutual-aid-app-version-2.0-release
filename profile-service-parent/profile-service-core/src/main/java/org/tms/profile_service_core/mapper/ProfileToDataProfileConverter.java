package org.tms.profile_service_core.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Profile;
import org.tms.profile_service_core.persistent.entity.Contact;
import org.tms.profile_service_core.persistent.entity.Name;

import java.util.ArrayList;

@Component
public class ProfileToDataProfileConverter extends BaseConverter<Profile, org.tms.profile_service_core.persistent.entity.Profile> {

    @Override
    protected org.tms.profile_service_core.persistent.entity.Profile getDestination() {
        return new org.tms.profile_service_core.persistent.entity.Profile();
    }

    @Override
    public void convert(Profile source, org.tms.profile_service_core.persistent.entity.Profile destination) {
        destination.setId(source.getId());
        destination.setProfileId(source.getProfileId());

        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setBirthday(source.getBirthday());
        destination.setGender(mapper.map(source.getGender()));

        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}