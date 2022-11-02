package org.tms.authservicerest.domain.service.ticket;

import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Ticket;

public interface TicketHandlerStrategy {

  Ticket.Type getTye();

  void addTicket(Profile profile);

  void verifyTicket(Profile profile, Ticket ticket);
}