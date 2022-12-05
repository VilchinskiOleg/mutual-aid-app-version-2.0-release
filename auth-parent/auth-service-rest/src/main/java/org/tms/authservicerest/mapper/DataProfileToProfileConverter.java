package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.persistent.model.Profile;
import org.tms.authservicerest.domain.model.Profile.Gender;
import java.util.HashSet;

@Component
public class DataProfileToProfileConverter extends BaseConverter<Profile, org.tms.authservicerest.domain.model.Profile> {

    @Override
    public void convert(Profile source, org.tms.authservicerest.domain.model.Profile destination) {
        destination.setProfileId(source.getProfileId());
        destination.setResourceId(source.getResourceId());
        destination.setGender(mapper.map(source.getGender(), Gender.class));
        destination.setBirthday(source.getBirthday());
        destination.setTickets(mapper.map(source.getTickets(), new HashSet<>(), Ticket.class));
        destination.setWeekPassword(source.isWeekPassword());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}