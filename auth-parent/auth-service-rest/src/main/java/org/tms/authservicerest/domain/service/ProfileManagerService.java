package org.tms.authservicerest.domain.service;

import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Ticket;

public interface ProfileManagerService {

  Profile create(Profile profile);

  String login(Ticket ticket);
}