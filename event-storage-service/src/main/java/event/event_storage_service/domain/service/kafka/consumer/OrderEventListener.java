package event.event_storage_service.domain.service.kafka.consumer;

import static java.util.Objects.nonNull;

import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.model.OrderEvent;
import event.event_storage_service.domain.service.EventStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class OrderEventListener {

    @Resource
    private EventStorageService eventStorageService;
    @Resource
    private Mapper mapper;

    @KafkaListener(topics = "${kafka.listenTopics}",
                   containerFactory = "orderEventListenerContainerFactory")
    public void saveOrderEvent(ConsumerRecord<String, KafkaOrderEvent> record, Consumer<String, KafkaOrderEvent> consumer) {
        KafkaOrderEvent kafkaOrderEvent = record.value();
        try {
            log.info("Process record: topic={}, key={}, offset={}, value={}",
                    record.topic(), record.key(), record.offset(), kafkaOrderEvent);
            if (nonNull(kafkaOrderEvent)) {
                OrderEvent orderEvent = mapper.map(kafkaOrderEvent, OrderEvent.class);
                eventStorageService.saveEvent(orderEvent);
            }
        } catch (Exception ex) {
            log.error("Unexpected error while processing record: topic={}, key={}, offset={}, value={}",
                    record.topic(), record.key(), record.offset(), kafkaOrderEvent, ex);
        } finally {
            consumer.commitSync();
        }
    }
}