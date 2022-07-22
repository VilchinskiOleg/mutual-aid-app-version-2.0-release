package org.tms.profile_service_rest.configuration.client;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import javax.annotation.Resource;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.stereotype.Component;

@Component
public class AuthRestClientAuthInterceptor implements RequestInterceptor {

  @Resource
  private AuthRestClientProperties authRestClientProperties;
  @Resource
  private CommonData commonData;

  @Override
  public void apply(RequestTemplate requestTemplate) {
    requestTemplate.header("user", authRestClientProperties.getUser());
    requestTemplate.header("password", authRestClientProperties.getPassword());
    requestTemplate.header(LANG_HEADER, commonData.getLocale().getLanguage());
  }
}