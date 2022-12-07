package org.tms.authservicerest.domain.service.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@Setter
public class JwtProperties {

  private String secret;
  private String issuer;
  // live token period:
  private long live;
}