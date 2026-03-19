package com.example.notificationconfig.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
public class NotificationContextConfig {

    private final NotificationKey notificationKey;
    private final List<String> payloadParams;
    private final Map<NotificationType, String> templatePathByNotificationType;

    public enum NotificationKey {

        RESET_WEEK_PASSWORD, RESET_PASSWORD;
    }

    public enum NotificationType {

        MAIL, PHONE;
    }
}
