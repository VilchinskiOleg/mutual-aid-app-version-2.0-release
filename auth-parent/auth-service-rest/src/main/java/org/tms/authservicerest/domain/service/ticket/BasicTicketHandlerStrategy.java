package org.tms.authservicerest.domain.service.ticket;

import javax.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Ticket;

public abstract class BasicTicketHandlerStrategy implements TicketHandlerStrategy {

  @Resource
  @Lazy
  private PasswordEncoder passwordEncoder;

  @Override
  public void verifyTicket(Profile profile, Ticket ticket) {
    var storedTicked = profile.findTicketByType(ticket.getType());
    checkPassword(ticket.getPassword(), storedTicked.getPassword());
  }

  protected void checkPassword(String inputPassword, String storedPassword) {
    if(!passwordEncoder.matches(inputPassword, storedPassword)) {
      throw new RuntimeException("Password is wrong!");
    }
  }

  protected String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}