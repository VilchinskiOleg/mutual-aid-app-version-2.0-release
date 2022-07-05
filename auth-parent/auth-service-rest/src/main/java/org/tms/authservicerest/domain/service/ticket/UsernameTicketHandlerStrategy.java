package org.tms.authservicerest.domain.service.ticket;

import static java.util.Objects.nonNull;
import static org.tms.authservicerest.domain.model.profile.Ticket.Type.USER_NAME;

import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Name;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.domain.model.profile.Ticket.Type;

@Component
public class UsernameTicketHandlerStrategy extends BasicTicketHandlerStrategy {

  @Override
  public Type getTye() {return USER_NAME;}

  @Override
  public void addTicket(Profile profile) {
    Name name = profile.getEnName();
    if (nonNull(name)) {
      profile.addTicket(new Ticket(name.getFirstName(), encodePassword(profile.getPassword()), getTye()));
    }
  }
}
