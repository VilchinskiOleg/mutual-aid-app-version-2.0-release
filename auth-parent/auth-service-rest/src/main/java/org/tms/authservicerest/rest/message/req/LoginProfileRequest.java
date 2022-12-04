package org.tms.authservicerest.rest.message.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.tms.authservicerest.rest.model.profile.Ticket;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginProfileRequest {

  private Ticket ticket;
}