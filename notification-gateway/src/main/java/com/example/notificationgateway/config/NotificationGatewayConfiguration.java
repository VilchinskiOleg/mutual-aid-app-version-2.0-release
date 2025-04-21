package com.example.notificationgateway.config;

import com.example.notificationgateway.model.NotificationContextConfig;
import com.example.notificationgateway.service.publisher.NotificationPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.*;
import java.util.stream.Collector;

import static com.example.notificationgateway.service.publisher.AbstractNotificationPublisher.NotificationKey;

@Configuration
@PropertySource(
        value = "classpath:application-notification-context-configuration.yml",
        factory = MultipleYamlPropertySourceFactory.class)
public class NotificationGatewayConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "notification-context-configs")
    public List<NotificationContextConfig> getNotificationContextConfigs() {
        return new ArrayList<>();
    }

    @Bean
    public EnumMap<NotificationKey, NotificationContextConfig> getCxtConfigsByNotificationKey(
            List<NotificationContextConfig> notificationContextConfigs) {
        return notificationContextConfigs.stream()
                .collect(Collector.of(
                            () -> new EnumMap<>(NotificationKey.class),
                            (map, element) -> map.putIfAbsent(element.getNotificationKey(), element),
                            (m1, m2) -> { m1.putAll(m2); return m1; })); // 'combiner' will not be used because stream is processed by single thread *
    }

    @Bean
    @Qualifier("publishersChain")
    public NavigableSet<NotificationPublisher> getNotificationPublishersChain() {
        return new TreeSet<>();
    }
}
