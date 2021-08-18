package event.event_storage_service.domain.service.kafka.consumer;

import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.service.EventStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class OrderEventListener {

    @Resource
    private EventStorageService eventStorageService;

    @KafkaListener(topics = "${kafka.listenTopics}")
    public void saveCreateOrderEvent(KafkaOrderEvent kafkaOrderEvent) {
        try {
            System.out.println(kafkaOrderEvent);
        } catch (KafkaException ex) {
            log.error("Something goes wrong!", ex);
        }
    }
}
