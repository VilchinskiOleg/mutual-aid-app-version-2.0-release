package messagechat.messagechatservice.configuration.client;

import messagechat.messagechatservice.configuration.client.profile.ProfileRestClientAuthInterceptor;
import messagechat.messagechatservice.configuration.client.profile.ProfileRestClientProperties;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

/**
 * Can use that config class to create all needed Interceptor-Beans for different clients.
 */
@Configuration
public class FeignInterceptorsConfiguration {

    @Resource
    private ProfileRestClientProperties profileRestClientProperties;

    @Bean
    public ProfileRestClientAuthInterceptor authRestClientAuthInterceptor(CommonData commonData) {
        return new ProfileRestClientAuthInterceptor(
                profileRestClientProperties.getUsername(),
                profileRestClientProperties.getPassword()
        );
    }
}