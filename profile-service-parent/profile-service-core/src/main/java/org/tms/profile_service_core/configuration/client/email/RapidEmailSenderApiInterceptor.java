package org.tms.profile_service_core.configuration.client.email;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import static org.tms.profile_service_core.utils.Constant.EmailSenderOpenApi.HOST_HEADER;
import static org.tms.profile_service_core.utils.Constant.EmailSenderOpenApi.TOKEN_HEADER;

@RequiredArgsConstructor
public class RapidEmailSenderApiInterceptor implements RequestInterceptor {

    private final String host;
    private final String token;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HOST_HEADER, host);
        template.header(TOKEN_HEADER, token);
    }
}