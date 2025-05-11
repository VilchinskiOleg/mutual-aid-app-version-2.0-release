package com.example.notificationconfig.config;

import com.example.notificationconfig.model.NotificationContextConfig;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Configuration
@PropertySource(
        value = {"classpath:application-notification-context-configuration.yml",
                "classpath:application-notification-types-per-key-configuration.yml"},
        factory = MultipleYamlPropertySourceFactory.class)
public class NotificationConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.application.name", havingValue = "notification-gateway-service")
    @ConfigurationProperties(prefix = "notification-context-config-items")
    public List<NotificationContextConfig> getNotificationContextConfigs() {
        return new ArrayList<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "notification-types-per-key-map")
    public Map<NotificationKey, List<NotificationType>> getNotificationTypesStrLinePerKey() {
        return new EnumMap<>(NotificationKey.class);
    }
}
