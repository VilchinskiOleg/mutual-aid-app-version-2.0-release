package order.orderservice.configuration.client;

import feign.Contract;
import feign.Logger;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

@Configuration
public class ProfileFeignClientConfig {

    @Resource
    private ProfileFeignClientProperties profileClientProps;

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
