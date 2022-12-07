package org.tms.task_executor_service.domain.service.client;

import static java.util.Objects.nonNull;
import static java.util.Set.of;
import static org.tms.task_executor_service.domain.model.Task.Type.CREATE_PROFILE;
import static org.tms.task_executor_service.utils.Constant.Errors.FAIL_PROFILE_CREATING;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.model.Error;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.config.client.ProfileRestClient;
import org.tms.task_executor_service.domain.model.Task.Type;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import org.tms.mutual_aid.profile_service.client.model.ProfileResponse;

@Component
@Slf4j
public class ProfileClientService implements TaskExecutionProvider<ProfileClientService> {

    @Resource
    private ProfileRestClient profileRestClient;
    @Resource
    private CommonData commonData;

    @Override
    public Set<Type> getSupportedTasks() {
        return of(CREATE_PROFILE);
    }

    @Override
    public ProfileClientService getProvider() {
        return this;
    }

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