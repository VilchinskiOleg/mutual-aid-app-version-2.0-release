package org.tms.authservicerest.configuration.client;

import static org.tms.authservicerest.utils.Constant.Service.EmailSenderOpenApi.HOST_HEADER;
import static org.tms.authservicerest.utils.Constant.Service.EmailSenderOpenApi.TOKEN_HEADER;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email-sender-open-api")
@Setter
public class RapidEmailSenderApiInterceptor implements RequestInterceptor {

    private String host;
    private String token;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HOST_HEADER, host);
        template.header(TOKEN_HEADER, token);
    }
}