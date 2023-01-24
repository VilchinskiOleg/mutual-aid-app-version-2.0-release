package org.tms.profile_service_rest.rest.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.tms.profile_service_rest.rest.model.Profile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse extends BaseResponse {

    private Profile profile;
}