package org.tms.profile_service_rest.configuration.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-rest-client")
@Getter
@Setter
public class AuthRestClientProperties {

    private String url;
    private String user;
    private String password;
}