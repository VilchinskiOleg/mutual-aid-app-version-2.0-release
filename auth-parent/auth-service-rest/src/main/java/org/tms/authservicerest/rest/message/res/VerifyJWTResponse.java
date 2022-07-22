package org.tms.authservicerest.rest.message.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.exception.handling.autoconfiguration.model.BaseResponse;

@Getter
@Setter
@ToString
public class VerifyJWTResponse extends BaseResponse {

  private Boolean success;
  private String profileId;
}