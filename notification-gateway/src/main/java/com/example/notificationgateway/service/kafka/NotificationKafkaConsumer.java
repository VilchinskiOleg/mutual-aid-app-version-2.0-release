package com.example.notificationgateway.service.kafka;

import com.example.notificationgateway.model.NotificationMessage;
import com.example.notificationgateway.service.publisher.NotificationPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.NavigableSet;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@DependsOn({"emailNotificationPublisher", "phoneNotificationPublisher"})
@Component
public class NotificationKafkaConsumer {

    private final NotificationPublisher notificationPublisher;
    private final ObjectMapper objectMapper;

    public NotificationKafkaConsumer(ObjectMapper objectMapper,
                     @Qualifier("publishersChain") NavigableSet<NotificationPublisher> notificationPublishersChain) {
        this.notificationPublisher = notificationPublishersChain.first();
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = {"${notification-gateway.kafka.topic}"})
    public void listenOneRecord(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.debug("Notification Record was received from partition - {}. With key - {} and offset - {}",
                record.partition(), record.key(), record.offset());
        try {
            process(record.value());
        } finally {
            ack.acknowledge();
        }
    }

    private void process(String notificationJsonStr) {
        if (isNotBlank(notificationJsonStr)) {
            try {
                NotificationMessage notification = objectMapper.readValue(notificationJsonStr, NotificationMessage.class);
                notificationPublisher.sendNotification(notification);
                return;
            } catch (JsonProcessingException e) {
                log.error("Json mapping failed with exception", e);
//                throw new RuntimeException(e); // чисто посмотреть что будет ..
            }

        }
        log.error("Wasn't able to convert kafka event - {} into Notification Message. Processing has been skipped",
                notificationJsonStr);
    }
}
