package org.tms.authservicerest.domain.service.ticket;

import static java.util.Objects.nonNull;
import static org.tms.authservicerest.domain.model.profile.Ticket.Type.EMAIL;
import static org.tms.authservicerest.domain.model.profile.Ticket.Type.PHONE;

import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Contact;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.domain.model.profile.Ticket.Type;

@Component
public class PhoneTicketHandlerStrategy extends ContactTicketHandlerStrategy {

  @Override
  public Type getTye() {return PHONE;}
}