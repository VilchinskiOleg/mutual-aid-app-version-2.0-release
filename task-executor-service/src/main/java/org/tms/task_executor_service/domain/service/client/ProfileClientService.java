package org.tms.task_executor_service.domain.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.model.Error;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import org.tms.mutual_aid.profile_service.client.model.ProfileResponse;
import org.tms.task_executor_service.config.client.ProfileRestClient;

import javax.annotation.Resource;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static org.tms.task_executor_service.utils.Constant.Errors.FAIL_PROFILE_CREATING;

@Component
@Slf4j
public class ProfileClientService implements CommandExecutionProvider {

    @Resource
    private ProfileRestClient profileRestClient;
    @Resource
    private CommonData commonData;

    public Profile createProfile(Profile profile) {
        String lang = commonData.getLocale().getLanguage();
        Response response = profileRestClient.createProfile(profile, lang);
        ProfileResponse profileResponse = deserializeResponse(response.body());
        checkResponse(profileResponse.getError(), profile);
        return profileResponse.getProfile();
    }

    private ProfileResponse deserializeResponse(Response.Body responseBody) {
        ObjectMapper jsonRider = new ObjectMapper();
        try {
            return jsonRider.readValue(responseBody.asInputStream(), ProfileResponse.class);
        } catch (IOException ex) {
            log.error("Unexpected error: cannot read response body", ex);
            throw new ConflictException(FAIL_PROFILE_CREATING);
        }
    }

    private void checkResponse(Error error, Profile profile) {
        if (nonNull(error)) {
            log.error("The request for creating profile={} is failed: {}", profile, error);
            throw new ConflictException(FAIL_PROFILE_CREATING);
        }
    }
}