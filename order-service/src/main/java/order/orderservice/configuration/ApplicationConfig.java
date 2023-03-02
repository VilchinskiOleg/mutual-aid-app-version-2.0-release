package order.orderservice.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableFeignClients(basePackages = "order.orderservice.configuration.client")
@EnableMongoRepositories(basePackages = "order.orderservice.persistent.mongo")
public class ApplicationConfig {
}