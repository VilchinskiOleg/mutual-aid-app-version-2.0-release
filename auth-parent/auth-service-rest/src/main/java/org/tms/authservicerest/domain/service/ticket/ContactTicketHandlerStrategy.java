package org.tms.authservicerest.domain.service.ticket;

import static java.util.Objects.nonNull;

import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Contact;
import org.tms.authservicerest.domain.model.profile.Ticket;

public abstract class ContactTicketHandlerStrategy extends BasicTicketHandlerStrategy {

  @Override
  public void addTicket(Profile profile) {
    Contact contact = profile.findContactByType(getTye().name());
    if (nonNull(contact)) {
      profile.addTicket(new Ticket(contact.getValue(), encodePassword(profile.getPassword()), getTye()));
    }
  }
}
