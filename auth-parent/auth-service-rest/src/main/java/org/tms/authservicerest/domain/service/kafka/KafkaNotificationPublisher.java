package org.tms.authservicerest.domain.service.kafka;

import com.example.notificationconfig.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationPublisher {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    @Value("${application.kafka.topic}")
    public String topic;

    public void publish(NotificationKey notificationKey, NotificationMessage notificationMsg) {
        log.debug("Try to send event to topic - {} with key - {}", topic, notificationKey);
        var result = kafkaTemplate.send(topic, notificationKey.toString(), notificationMsg);

        result.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Message sending failed", ex);
            }

            @Override
            public void onSuccess(SendResult<String, NotificationMessage> eventSendResult) {
                int partition = eventSendResult.getRecordMetadata().partition();
                log.debug("Message was sent successfully and assigned to partition - {}, having key - {}",
                        partition, notificationKey);
            }
        });
    }
}
