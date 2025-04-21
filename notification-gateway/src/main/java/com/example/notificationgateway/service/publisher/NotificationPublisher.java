package com.example.notificationgateway.service.publisher;

import com.example.notificationgateway.model.NotificationMessage;

public interface NotificationPublisher extends Comparable<NotificationPublisher> {

    void sendNotification(NotificationMessage notificationMessage);
}
