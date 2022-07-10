package org.tms.common.auth.configuration.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth-rest-client")
@ConditionalOnProperty(prefix = "auth-rest-client", name = "url", matchIfMissing = false)

@Getter
@Setter
public class AuthRestClientProperties {

  private String url;
  @Value("${auth-rest-client.read-timeout}")
  private Integer readTimeout;
  @Value("${auth-rest-client.connection-timeout}")
  private Integer connectionTimeout;
}