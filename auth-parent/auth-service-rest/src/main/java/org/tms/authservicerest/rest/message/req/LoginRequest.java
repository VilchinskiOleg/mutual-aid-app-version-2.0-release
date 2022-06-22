package org.tms.authservicerest.rest.message.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.authservicerest.rest.model.Profile;

@Getter
@Setter
@ToString
public class LoginRequest {

  private Profile profile;
}