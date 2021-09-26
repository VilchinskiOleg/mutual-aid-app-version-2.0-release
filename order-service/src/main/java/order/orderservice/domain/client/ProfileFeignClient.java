package order.orderservice.domain.client;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import order.orderservice.configuration.client.ProfileFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ort.tms.mutual_aid.profile_service.client.model.ProfileResponse;

@FeignClient(
        value = "profileClient",
        url = "${profile-rest.url}",
        configuration = ProfileFeignClientConfig.class)
public interface ProfileFeignClient {

    @GetMapping("/{profile-id}")
    ProfileResponse getProfileByInternalId(@PathVariable("profile-id") String profileId,
                                           @RequestHeader(LANG_HEADER) String lang);
}
