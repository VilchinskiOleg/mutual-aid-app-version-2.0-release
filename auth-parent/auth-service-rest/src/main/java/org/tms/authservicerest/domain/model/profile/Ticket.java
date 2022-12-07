package org.tms.authservicerest.domain.model.profile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  That representation-model describes some details for login by type.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

  @Include
  private String login;
  private String password;
  @Include
  private Type type;

  public enum Type {

    EMAIL,
    PHONE,
    USER_NAME
  }
}