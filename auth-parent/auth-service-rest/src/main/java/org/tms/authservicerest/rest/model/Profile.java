package org.tms.authservicerest.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Profile {

  private String login;
  private String password;
}