package com.example.notificationgatewayservice.service.kafka;

import com.example.notificationconfig.model.NotificationMessage;
import com.example.notificationgatewayservice.service.publisher.NotificationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.NavigableSet;

@Slf4j
@DependsOn({"emailNotificationPublisher", "phoneNotificationPublisher"})
@Component
public class NotificationKafkaConsumer {

    private final NotificationPublisher notificationPublisher;

    public NotificationKafkaConsumer(@Qualifier("publishersChain") NavigableSet<NotificationPublisher> notifPublishChain) {
        this.notificationPublisher = notifPublishChain.first();
    }

    @KafkaListener(topics = {"${notification-gateway.kafka.topic}"})
    public void listenOneRecord(ConsumerRecord<String, NotificationMessage> record, Acknowledgment ack) {
        log.info("Notification Record was received from partition - {}. With key - {} and offset - {}",
                record.partition(), record.key(), record.offset());
        try {
            process(record.value());
        } finally {
            ack.acknowledge();
        }
    }

    private void process(NotificationMessage notificationMsg) {
        if (!validateNotificationMsg(notificationMsg)) {
            log.error("Notification message = {} invalid", notificationMsg);
        }
        try {
            notificationPublisher.sendNotification(notificationMsg);
        } catch (Exception e) {
            log.error("Wasn't able to process notification message = {} because of :", notificationMsg, e);
        }
    }

    private boolean validateNotificationMsg(NotificationMessage notificationMsg) {
        return true; //todo: implement..
    }
}
