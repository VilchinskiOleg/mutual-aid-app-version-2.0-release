package messagechat.messagechatservice.configuration.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "application.api-info")
public class ApiProperties {

    private String version;
    private String title;
    private String description;
    private Contact contact;

    @Getter
    @Setter
    public static class Contact {

        private String name;
        private String url;
        private String email;
    }
}
