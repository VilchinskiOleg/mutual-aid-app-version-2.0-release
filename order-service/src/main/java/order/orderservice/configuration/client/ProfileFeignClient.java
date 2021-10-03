package order.orderservice.configuration.client;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "profileClient", url = "${profile-rest.url}")
public interface ProfileFeignClient {

    @GetMapping("/{profile-id}")
    Response getProfileByInternalId(@PathVariable("profile-id") String profileId,
                                    @RequestHeader(LANG_HEADER) String lang);
}
