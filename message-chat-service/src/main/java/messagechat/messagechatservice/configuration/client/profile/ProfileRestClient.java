package messagechat.messagechatservice.configuration.client.profile;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "profileRestClient", url = "${profile-rest-client.url}")
public interface ProfileRestClient {

    @GetMapping("/api/profile-service/{profile-id}")
    Response getProfileByInternalId(@PathVariable("profile-id") String profileId,
                                    @RequestHeader(LANG_HEADER) String lang);
}