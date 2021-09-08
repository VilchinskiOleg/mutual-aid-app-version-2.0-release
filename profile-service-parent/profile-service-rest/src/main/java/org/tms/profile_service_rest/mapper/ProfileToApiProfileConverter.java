package org.tms.profile_service_rest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Profile;
import org.tms.profile_service_rest.rest.model.Contact;
import org.tms.profile_service_rest.rest.model.Name;
import java.util.ArrayList;

@Component
public class ProfileToApiProfileConverter extends BaseConverter<Profile, org.tms.profile_service_rest.rest.model.Profile> {

    @Override
    protected org.tms.profile_service_rest.rest.model.Profile getDestination() {
        return new org.tms.profile_service_rest.rest.model.Profile();
    }

    @Override
    public void convert(Profile source, org.tms.profile_service_rest.rest.model.Profile destination) {
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setBirthday(source.getBirthday());
        destination.setGender(mapper.map(source.getGender()));

        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setProfileId(source.getProfileId());
        destination.setId(source.getId());
    }
}
