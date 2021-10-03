package order.orderservice.configuration.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "profile-rest")
@Getter
@Setter
public class ProfileFeignClientProperties {

    private String url;
}
