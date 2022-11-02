package org.tms.common.auth.configuration.client.config;

import static java.util.Collections.singletonList;

import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.tms.common.auth.configuration.client.AuthRestClientProperties;

@Configuration
@ConditionalOnBean(name = "authRestClientProperties")
public class RestClientConfig {

  @Resource
  private AuthRestClientProperties authRestClientProperties;

  @Bean(name = "restApi")
  public RestTemplate getRestApi() {
    final var requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setReadTimeout(authRestClientProperties.getReadTimeout());
    requestFactory.setConnectTimeout(authRestClientProperties.getConnectionTimeout());
    final var restApi = new RestTemplate(requestFactory);
    restApi.setMessageConverters(singletonList(new MappingJackson2HttpMessageConverter()));
    restApi.setErrorHandler(new CustomResponseErrorHandler());
    return restApi;
  }
}