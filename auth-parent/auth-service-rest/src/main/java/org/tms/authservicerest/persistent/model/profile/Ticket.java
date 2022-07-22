package org.tms.authservicerest.persistent.model.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {

  // Add indexes for 'login' and 'type';
  private String login;
  private String password;
  private String type;
}