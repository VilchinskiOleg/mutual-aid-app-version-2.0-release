package org.tms.profile_service_core.configuration.client.auth;

import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.common.http.autoconfiguration.model.CommonData;

import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

public class AuthRestClientAuthInterceptor extends BasicAuthRequestInterceptor {

  private CommonData commonData;

  public AuthRestClientAuthInterceptor(String username, String password, CommonData commonData) {
    super(username, password);
    this.commonData = commonData;
  }

  @Override
  public void apply(RequestTemplate template) {
    super.apply(template);
    template.header(LANG_HEADER, commonData.getLocale().getLanguage());
  }
}