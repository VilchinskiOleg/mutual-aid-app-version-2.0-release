package org.tms.task_executor_service.config.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "profile-rest-client")
public class ProfileRestClientProperties {

    private String url;
}