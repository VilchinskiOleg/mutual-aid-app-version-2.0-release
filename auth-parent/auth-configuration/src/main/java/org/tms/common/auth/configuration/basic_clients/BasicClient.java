package org.tms.common.auth.configuration.basic_clients;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicClient {

  private String name;
  private String password;
  private Set<String> roles;
}