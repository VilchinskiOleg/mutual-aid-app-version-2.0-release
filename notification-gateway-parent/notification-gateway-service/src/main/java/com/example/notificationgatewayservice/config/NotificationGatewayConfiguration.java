package com.example.notificationgatewayservice.config;

import com.example.notificationconfig.config.NotificationConfiguration;
import com.example.notificationconfig.model.NotificationContextConfig;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;

import com.example.notificationgatewayservice.service.publisher.NotificationPublisher;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.EnumMap;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collector;

@Configuration
@Import(NotificationConfiguration.class) // As alternative instead of spring.factory
public class NotificationGatewayConfiguration {

    @Bean
    public EnumMap<NotificationKey, NotificationContextConfig> getCxtConfigsByNotificationKey(
            List<NotificationContextConfig> notificationContextConfigs) {
        return notificationContextConfigs.stream()
                .collect(Collector.of(
                            () -> new EnumMap<>(NotificationKey.class),
                            (map, element) -> map.putIfAbsent(element.getNotificationKey(), element),
                            // 'combiner' will not be used because stream is processed by single thread
                            // (ParallelStream is not used now) :
                            (m1, m2) -> { m1.putAll(m2); return m1; }));
    }

    @Bean
    @Qualifier("publishersChain")
    public NavigableSet<NotificationPublisher> getNotificationPublishersChain() {
        return new TreeSet<>();
    }
}
