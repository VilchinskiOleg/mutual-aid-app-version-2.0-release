package com.example.notificationgatewayservice.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContext {

    String title;
    String messageTemplatePath;
    Map<String, Object> templateVariables;
    String recipientAddress;
}
