package org.tms.profile_service_rest.domain.service.client;

import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;
import static org.tms.profile_service_rest.utils.Constant.Errors.FAIL_CREATING_AUTH_PROFILE;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Response.Body;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.auth.client.model.CreateProfileRequest;
import org.tms.mutual_aid.auth.client.model.CreateProfileResponse;
import org.tms.profile_service_rest.configuration.client.AuthRestClient;
import org.tms.profile_service_rest.domain.model.Profile;

@Component
@Slf4j
public class AuthClientService {

    @Resource
    private AuthRestClient authApi;
    @Resource
    private Mapper mapper;

    public void createAuth(Profile profile) {
        final var request = createRequest(profile);
        Response response = authApi.create(request);

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
            log.error("The request for create auth profile by id={} is failed: {}", profileId, authResponse.getError());
            throw new ConflictException(FAIL_CREATING_AUTH_PROFILE);
        }
    }
}