package order.orderservice.domain.service.client;

import static order.orderservice.util.Constant.Errors.DID_NOT_MANAGE_RETRIEVE_PROFILE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Response.Body;
import lombok.extern.slf4j.Slf4j;
import order.orderservice.configuration.client.profile.ProfileRestClient;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import org.tms.mutual_aid.profile_service.client.model.ProfileResponse;
import javax.annotation.Resource;
import java.io.IOException;

@Component
@Slf4j
public class ProfileClientService {

    @Resource
    private ProfileRestClient profileApi;
    @Resource
    private CommonData commonData;

    public Profile getProfileById(String profileId) {
        if (isBlank(profileId)) {
            log.error("Unexpected error: profileId={} incorrect, request was declined", profileId);
            throw new ConflictException(DID_NOT_MANAGE_RETRIEVE_PROFILE);
        }
        String lang = commonData.getLocale().getLanguage();
        Response response = profileApi.getProfileByInternalId(profileId, lang);
        ProfileResponse profileResponse = deserializeResponse(response.body());
        checkResponse(response.status(), profileResponse, profileId);
        return profileResponse.getProfile();
    }

    private ProfileResponse deserializeResponse(Body responseBody) {
        ObjectMapper jsonRider = new ObjectMapper();
        try {
            return jsonRider.readValue(responseBody.asInputStream(), ProfileResponse.class);
        } catch (IOException ex) {
            log.error("Unexpected error: cannot read response body", ex);
            throw new ConflictException(DID_NOT_MANAGE_RETRIEVE_PROFILE);
        }
    }

    private void checkResponse(int statusCode, ProfileResponse profileResponse, String profileId) {
        if (statusCode != OK_HTTP_CODE) {
            log.error("The request for getting profile by id={} is failed: {}", profileId, profileResponse.getError());
            throw new ConflictException(DID_NOT_MANAGE_RETRIEVE_PROFILE);
        }
    }
}