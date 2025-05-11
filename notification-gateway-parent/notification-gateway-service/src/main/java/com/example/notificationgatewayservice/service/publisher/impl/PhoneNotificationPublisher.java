package com.example.notificationgatewayservice.service.publisher.impl;

import com.example.notificationconfig.model.NotificationContextConfig;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import com.example.notificationgatewayservice.model.NotificationContext;
import com.example.notificationgatewayservice.service.ProfileService;
import com.example.notificationgatewayservice.service.publisher.AbstractNotificationPublisher;
import com.example.notificationgatewayservice.service.publisher.NotificationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NavigableSet;

@Slf4j
@Component
public class PhoneNotificationPublisher extends AbstractNotificationPublisher {

    public PhoneNotificationPublisher(@Qualifier("publishersChain") NavigableSet<NotificationPublisher> publishersChain,
                                      Map<NotificationKey, NotificationContextConfig> ctxConfigsByNotificationKey,
                                      ProfileService profileService) {
        super(publishersChain, ctxConfigsByNotificationKey, profileService);
    }

    @Override
    protected void doSendNotification(NotificationContext notificationContext) {
        // todo..
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.PHONE;
    }
}
