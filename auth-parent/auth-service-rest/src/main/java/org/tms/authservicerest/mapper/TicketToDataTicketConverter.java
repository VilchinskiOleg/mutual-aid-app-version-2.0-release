package org.tms.authservicerest.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.profile.Ticket;

@Component
public class TicketToDataTicketConverter extends
    BaseConverter<Ticket, org.tms.authservicerest.persistent.model.profile.Ticket> {

  @Override
  public void convert(Ticket source, org.tms.authservicerest.persistent.model.profile.Ticket destination) {
    System.out.println("OK!");
  }
}