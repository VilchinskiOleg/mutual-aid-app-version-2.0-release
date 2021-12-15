package messagechat.messagechatservice.configuration.client.profile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "profile-service-client")
public class ProfileRestClientProperties {

    private String url;
}