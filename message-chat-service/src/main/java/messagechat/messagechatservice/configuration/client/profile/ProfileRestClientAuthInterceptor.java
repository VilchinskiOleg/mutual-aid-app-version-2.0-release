package messagechat.messagechatservice.configuration.client.profile;

import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.common.http.autoconfiguration.model.CommonData;

import static java.util.Objects.nonNull;
import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

public class ProfileRestClientAuthInterceptor extends BasicAuthRequestInterceptor {

  private CommonData commonData;

  public ProfileRestClientAuthInterceptor(String username, String password) {
    super(username, password);
  }

  @Override
  public void apply(RequestTemplate template) {
    super.apply(template);
  }
}