package org.tms.authservicerest.rest.model;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {

  private String login;
  private String password; // only for input
  @Pattern(regexp = "^(USER_NAME|EMAIL|PHONE)$", message = "TICKET_TYPE_RULE")
  private String type;
}