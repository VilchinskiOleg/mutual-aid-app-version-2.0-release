package org.tms.authservicerest.configuration.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.tms.authservicerest.configuration.client.model.EmailLetter;

/**
 * As open api I use 'SendGrid'.
 */
@FeignClient(name = "emailSenderApiClient", url = "${email-sender-open-api.url}")
public interface RapidEmailSenderApiClient {

    @PostMapping
    Response sendEmail(@RequestBody EmailLetter email);
}