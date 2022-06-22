package org.tms.common.auth.configuration.client;

import static java.util.Collections.singletonList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

  @Value("${auth-rest-client.read-timeout}")
  private static Integer READ_TIMEOUT;
  @Value("${auth-rest-client.connection-timeout}")
  private static Integer CONNECTION_TIMEOUT;

  @Bean(name = "restApi")
  public RestTemplate getRestApi() {
    final var requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setReadTimeout(READ_TIMEOUT);
    requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
    final var restApi = new RestTemplate(requestFactory);
    restApi.setMessageConverters(singletonList(new MappingJackson2HttpMessageConverter()));
    return restApi;
  }
}