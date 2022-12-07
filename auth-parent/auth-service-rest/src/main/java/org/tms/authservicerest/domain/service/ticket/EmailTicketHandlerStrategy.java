package org.tms.authservicerest.domain.service.ticket;

import static org.tms.authservicerest.domain.model.profile.Ticket.Type.EMAIL;

import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.profile.Ticket.Type;

@Component
public class EmailTicketHandlerStrategy extends ContactTicketHandlerStrategy {
  @Override
  public Type getTye() {return EMAIL;}
}