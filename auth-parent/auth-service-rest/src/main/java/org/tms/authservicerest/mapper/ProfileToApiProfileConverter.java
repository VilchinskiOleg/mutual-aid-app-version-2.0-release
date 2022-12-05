package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;

@Component
public class ProfileToApiProfileConverter extends BaseConverter<Profile, org.tms.authservicerest.rest.model.Profile> {

    @Override
    public void convert(Profile source, org.tms.authservicerest.rest.model.Profile destination) {
        destination.setId(source.getProfileId());
        destination.setResourceId(source.getResourceId());
        destination.setGender(mapper.map(source.getGender()));
        destination.setBirthday(source.getBirthday());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}