package org.tms.profile_service_rest.mapper.client;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Profile;

@Component
public class ProfileToAuthProfileConverter extends BaseConverter<Profile, org.tms.mutual_aid.auth.client.model.Profile> {

  @Override
  public void convert(Profile source, org.tms.mutual_aid.auth.client.model.Profile destination) {

  }
}