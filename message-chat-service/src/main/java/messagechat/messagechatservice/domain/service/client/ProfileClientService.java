package messagechat.messagechatservice.domain.service.client;

import static java.util.Objects.nonNull;
import static messagechat.messagechatservice.util.Constant.Errors.MEMBER_NOT_FUND;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.configuration.client.profile.ProfileRestClient;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.model.Error;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import org.tms.mutual_aid.profile_service.client.model.ProfileResponse;
import java.io.IOException;

@Slf4j
@Component
public class ProfileClientService {

    @Resource
    private ProfileRestClient profileRestClient;
    @Resource
    private CommonData commonData;

    public Profile getProfileById(String profileId) {
        if (isNotBlank(profileId)) {
            String lang = commonData.getLocale().getLanguage();
            Response response = profileRestClient.getProfileByInternalId(profileId, lang);
            ProfileResponse profileResponse = deserializeResponse(response.body());
            checkResponse(profileResponse.getError(), profileId);
            return profileResponse.getProfile();
        }
        log.error("Unexpected error: profileId={} incorrect, request was skipped", profileId);
        return null;
    }

    private ProfileResponse deserializeResponse(Response.Body responseBody) {
        ObjectMapper jsonRider = new ObjectMapper();
        try {
            return jsonRider.readValue(responseBody.asInputStream(), ProfileResponse.class);
        } catch (IOException ex) {
            log.error("Unexpected error: cannot read response body", ex);
            throw new ConflictException(MEMBER_NOT_FUND);
        }
    }

    private void checkResponse(Error error, String profileId) {
        if (nonNull(error)) {
            log.error("The request for getting profile by id={} is failed: {}", profileId, error);
            throw new ConflictException(MEMBER_NOT_FUND);
        }
    }
}