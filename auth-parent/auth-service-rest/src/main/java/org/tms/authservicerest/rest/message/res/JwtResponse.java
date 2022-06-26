package org.tms.authservicerest.rest.message.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.exception.handling.autoconfiguration.model.BaseResponse;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse extends BaseResponse {

  private String jwt;
}