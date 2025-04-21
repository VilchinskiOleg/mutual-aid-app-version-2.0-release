package com.example.notificationgateway.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContext {

    String messageTemplatePath;
    Map<String, Object> templateVariables;
    String recipientAddress;
}
