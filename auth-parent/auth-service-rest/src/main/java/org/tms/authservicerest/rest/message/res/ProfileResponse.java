package org.tms.authservicerest.rest.message.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.tms.authservicerest.rest.model.Profile;

@Getter
@Setter
@ToString
public class ProfileResponse extends BaseResponse {

  private Profile profile;
}