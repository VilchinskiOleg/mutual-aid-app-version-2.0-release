package event.event_storage_service.domain.service.kafka.consumer;

import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.model.OrderEvent;
import event.event_storage_service.domain.service.EventStorageService;
import lombok.extern.slf4j.Slf4j;
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

    @KafkaListener(topics = "${kafka.listenTopics}")
    public void saveCreateOrderEvent(KafkaOrderEvent kafkaOrderEvent) {
        try {
            OrderEvent orderEvent = mapper.map(kafkaOrderEvent, OrderEvent.class);
            eventStorageService.saveEvent(orderEvent);
        } catch (Exception ex) {
            log.error("Unexpected exception: cannot process event {}: ", kafkaOrderEvent, ex);
        }
    }
}
