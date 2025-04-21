package com.example.notificationgateway.service.publisher.impl;

import com.example.notificationgateway.model.NotificationContext;
import com.example.notificationgateway.model.NotificationContextConfig;
import com.example.notificationgateway.service.ProfileService;
import com.example.notificationgateway.service.publisher.AbstractNotificationPublisher;
import com.example.notificationgateway.service.publisher.NotificationPublisher;
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
