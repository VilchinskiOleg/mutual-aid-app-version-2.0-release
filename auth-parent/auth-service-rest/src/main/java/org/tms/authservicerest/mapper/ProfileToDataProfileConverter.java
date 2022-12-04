package org.tms.authservicerest.mapper;

import java.util.HashSet;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.persistent.model.profile.Ticket;

@Component
public class ProfileToDataProfileConverter extends
    BaseConverter<Profile, org.tms.authservicerest.persistent.model.Profile> {

  @Override
  public void convert(Profile source, org.tms.authservicerest.persistent.model.Profile destination) {
    destination.setProfileId(source.getProfileId());
    destination.setResourceId(source.getResourceId());
    destination.setGender(mapper.map(source.getGender()));
    destination.setBirthday(source.getBirthday());
    destination.setTickets(mapper.map(source.getTickets(), new HashSet<>(), Ticket.class));
    destination.setWeekPassword(source.isWeekPassword());
  }
}