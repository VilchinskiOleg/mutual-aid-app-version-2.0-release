package org.tms.authservicerest.domain.model.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact {

  private Type type;
  private String value;

  public enum Type {
    EMAIL,
    PHONE
  }
}