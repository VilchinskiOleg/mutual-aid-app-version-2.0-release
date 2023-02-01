package order.orderservice.configuration.client;

import order.orderservice.configuration.client.profile.ProfileRestClientAuthInterceptor;
import order.orderservice.configuration.client.profile.ProfileRestClientProperties;
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
    public ProfileRestClientAuthInterceptor profileRestClientAuthInterceptor() {
        return new ProfileRestClientAuthInterceptor(
                profileRestClientProperties.getUser(),
                profileRestClientProperties.getPassword()
        );
    }
}