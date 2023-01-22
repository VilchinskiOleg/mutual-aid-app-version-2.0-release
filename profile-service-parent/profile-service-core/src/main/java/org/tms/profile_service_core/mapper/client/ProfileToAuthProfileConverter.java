package org.tms.profile_service_core.mapper.client;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.auth.client.model.Contact;
import org.tms.mutual_aid.auth.client.model.Name;
import org.tms.profile_service_core.domain.model.Profile;

import java.util.ArrayList;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class ProfileToAuthProfileConverter extends BaseConverter<Profile, org.tms.mutual_aid.auth.client.model.Profile> {

  @Override
  public void convert(Profile source, org.tms.mutual_aid.auth.client.model.Profile destination) {
    destination.setResourceId(source.getProfileId());
    destination.setBirthday(source.getBirthday().format(ofPattern("yyyy-MM-dd")));
    destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
    destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
    destination.setGender(mapper.map(source.getGender()));
    if (isNotBlank(source.getPassword())) {
      destination.setPassword(source.getPassword());
    }
  }
}