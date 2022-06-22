package org.tms.authservicerest.domain.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Profile {

  private String login;
  private String password;
  private Map<String, Object> ticket;
}