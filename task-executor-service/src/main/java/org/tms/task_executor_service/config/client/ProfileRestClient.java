package org.tms.task_executor_service.config.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ort.tms.mutual_aid.profile_service.client.model.Profile;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

@FeignClient(name = "profileRestClient", url = "${profile-rest-client.url}")
public interface ProfileRestClient {

    @PostMapping("/api/profile-service")
    Response createProfile(@RequestBody Profile profile,
                           @RequestHeader(LANG_HEADER) String lang);
}