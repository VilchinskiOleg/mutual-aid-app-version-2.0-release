package org.tms.authservicerest.domain.service;

import com.example.notificationconfig.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.service.kafka.KafkaNotificationPublisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import static com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import static java.util.regex.Pattern.compile;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationPublisherService {

    private final KafkaNotificationPublisher kafkaNotificationPublisher;
    private final Map<NotificationKey, List<NotificationType>> notificationTypesPerKey;

    public void notify(NotificationKey notificationKey, Profile profile) {
        NotificationMessage msg = buildMsg(notificationKey, profile);
        kafkaNotificationPublisher.publish(notificationKey, msg);
    }

    private NotificationMessage buildMsg(NotificationKey notificationKey, Profile profile) {
        return NotificationMessage.builder()
                .notificationKey(notificationKey)
                .notificationTypes(notificationTypesPerKey.get(notificationKey))
                .recipientId(profile.getResourceId())
                .payload(buildPayload(profile))
                .build();
    }

    private Map<String, Object> buildPayload(Profile profile) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("defaultPassword", profile.getPassword());
        payload.put("resetPasswordLink", generateUrlToResetPassword());
        payload.put("userName", profile.getEnName().getFirstName());
        return payload;
    }




    private String generateUrlToResetPassword() {
        return "https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/"; // MOCK
    }

    private static final Pattern SUCCESS_HTTP_CODE = compile("^2\\d+$");

//    private void checkResponse(Response response) {
//        if (!SUCCESS_HTTP_CODE.matcher(valueOf(response.status())).matches()) {
//            throw new ConflictException(RESET_PASSWORD_FAILED);
//        }
//    }
}