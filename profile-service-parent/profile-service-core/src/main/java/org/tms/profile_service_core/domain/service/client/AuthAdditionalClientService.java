package org.tms.profile_service_core.domain.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Response.Body;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.auth.client.model.CreateProfileRequest;
import org.tms.mutual_aid.auth.client.model.CreateProfileResponse;
import org.tms.profile_service_core.configuration.client.auth.AuthRestFeignClient;
import org.tms.profile_service_core.domain.model.Profile;

import javax.annotation.Resource;
import java.io.IOException;

import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;
import static org.tms.profile_service_core.utils.Constant.Errors.FAIL_CREATING_AUTH_PROFILE;

@Component
@Slf4j
public class AuthAdditionalClientService {

    @Resource
    private AuthRestFeignClient authRestFeignClient;
    @Resource
    private Mapper mapper;

    public void createAuth(Profile profile) {
        final var request = createRequest(profile);
        Response response = authRestFeignClient.create(request);

        CreateProfileResponse authResponse = deserializeResponse(response.body());
        checkResponse(response.status(), authResponse, profile.getProfileId());
    }

    private CreateProfileRequest createRequest(Profile profile) {
        var authProfile = mapper.map(profile, org.tms.mutual_aid.auth.client.model.Profile.class);
        var request = new CreateProfileRequest();
        request.setProfile(authProfile);
        return request;
    }

    private CreateProfileResponse deserializeResponse(Body responseBody) {
        ObjectMapper jsonRider = new ObjectMapper();
        try {
            return jsonRider.readValue(responseBody.asInputStream(), CreateProfileResponse.class);
        } catch (IOException ex) {
            log.error("Unexpected error: cannot read response body", ex);
            throw new ConflictException(FAIL_CREATING_AUTH_PROFILE);
        }
    }

    private void checkResponse(int statusCode, CreateProfileResponse authResponse, String profileId) {
        if (statusCode != OK_HTTP_CODE) {
            log.error("The request for create auth profile by resourceId={} is failed: \n\tstatus = {} \n\terror = {}", profileId, statusCode, authResponse.getError());
            throw new ConflictException(FAIL_CREATING_AUTH_PROFILE);
        }
    }
}