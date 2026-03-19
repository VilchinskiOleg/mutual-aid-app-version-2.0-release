package com.example.notificationgatewayservice.service.publisher;


import com.example.notificationconfig.model.NotificationMessage;

public interface NotificationPublisher extends Comparable<NotificationPublisher> {

    void sendNotification(NotificationMessage notificationMessage);
}
