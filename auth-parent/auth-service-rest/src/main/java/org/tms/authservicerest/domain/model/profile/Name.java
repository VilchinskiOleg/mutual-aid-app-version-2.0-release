package org.tms.authservicerest.domain.model.profile;

import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Name {

  private Locale locale;
  private String firstName;
  private String lastName;
}