package org.tms.authservicerest.persistent.service;

import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.persistent.model.Profile;

public interface ExtendedProfileRepository {

  Profile findProfileByTicket(Ticket ticket);
}