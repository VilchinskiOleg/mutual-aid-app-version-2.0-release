package org.tms.profile_service_rest.configuration.client.auth;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.tms.mutual_aid.auth.client.model.CreateProfileRequest;

@FeignClient(name = "authRestFeignClient", url = "${auth-rest-client.url}")
public interface AuthRestFeignClient {

    @PostMapping("/api/auth-service/create")
    Response create(@RequestBody CreateProfileRequest request);
}