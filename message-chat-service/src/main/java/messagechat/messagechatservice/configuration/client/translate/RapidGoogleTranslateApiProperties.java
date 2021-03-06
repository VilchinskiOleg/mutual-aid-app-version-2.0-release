package messagechat.messagechatservice.configuration.client.translate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "google-translate-open-api")
public class RapidGoogleTranslateApiProperties {

    private String url;
    private String host;
    private String token;
    private String encoding;
}
