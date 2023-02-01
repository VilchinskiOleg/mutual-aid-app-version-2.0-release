package order.orderservice.configuration.client.profile;

import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.common.http.autoconfiguration.model.CommonData;

public class ProfileRestClientAuthInterceptor extends BasicAuthRequestInterceptor {


  public ProfileRestClientAuthInterceptor(String username, String password) {
    super(username, password);
  }

  @Override
  public void apply(RequestTemplate template) {
    super.apply(template);
    //... can add my own logic: for example any headers.
  }
}