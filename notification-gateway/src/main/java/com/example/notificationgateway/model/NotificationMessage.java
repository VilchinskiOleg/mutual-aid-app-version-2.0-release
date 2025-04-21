package com.example.notificationgateway.model;

import com.example.notificationgateway.service.publisher.AbstractNotificationPublisher.NotificationKey;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.example.notificationgateway.service.publisher.AbstractNotificationPublisher.NotificationType;

@Getter
@Setter
@Builder
public class NotificationMessage implements Serializable {

    private NotificationKey notificationKey;
    private List<NotificationType> notificationTypes;
    private String recipientId; // equivalent 'profileId'
    private Map<String, Object> payload;
}
