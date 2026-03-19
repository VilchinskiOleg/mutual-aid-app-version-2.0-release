package com.example.notificationconfig.model;

import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage implements Serializable {

    private NotificationKey notificationKey;
    private List<NotificationType> notificationTypes;
    private String recipientId; // equivalent 'profileId'
    private Map<String, Object> payload;
}
