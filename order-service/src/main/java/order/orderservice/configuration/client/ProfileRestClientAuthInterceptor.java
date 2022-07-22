package order.orderservice.configuration.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProfileRestClientAuthInterceptor implements RequestInterceptor {

  @Resource
  private ProfileRestClientProperties profileRestClientProperties;

  @Override
  public void apply(RequestTemplate requestTemplate) {
    requestTemplate.header("user", profileRestClientProperties.getUser());
    requestTemplate.header("password", profileRestClientProperties.getPassword());
  }
}