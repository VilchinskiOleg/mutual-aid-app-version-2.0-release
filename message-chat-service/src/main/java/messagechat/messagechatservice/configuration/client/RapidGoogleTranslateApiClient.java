package messagechat.messagechatservice.configuration.client;

import static messagechat.messagechatservice.util.Constant.Service.GoogleTranslateApi.*;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleTranslateApiClient", url = "${google-translate-open-api.url}")
public interface RapidGoogleTranslateApiClient {

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    Response translate(@RequestHeader(HOST_HEADER) String host,
                       @RequestHeader(TOKEN_HEADER) String token,
                       @RequestHeader(ENCODING_HEADER) String acceptEncoder,
                       @RequestBody String formRequestBody);
}