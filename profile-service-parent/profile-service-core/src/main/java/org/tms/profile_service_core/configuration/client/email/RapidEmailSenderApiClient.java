package org.tms.profile_service_core.configuration.client.email;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.tms.profile_service_core.configuration.client.email.model.EmailLetter;

/**
 * As open api I use 'SendGrid'.
 */
@FeignClient(name = "emailSenderApiClient", url = "${email-sender-open-api.url}")
public interface RapidEmailSenderApiClient {

    @PostMapping
    Response sendEmail(@RequestBody EmailLetter email);
}