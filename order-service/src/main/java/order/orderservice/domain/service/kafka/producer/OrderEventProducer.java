package order.orderservice.domain.service.kafka.producer;

import static java.time.LocalDateTime.now;
import static order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import static order.orderservice.util.Constant.Kafka.*;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import javax.annotation.Resource;

@Slf4j
public abstract class OrderEventProducer {

    @Resource
    private KafkaTemplate<String, KafkaOrderEvent> orderEventTemplate;
    @Resource
    private Mapper mapper;

    public void sendMessage(Order order) {
        KafkaOrderEvent orderEvent = mapper.map(order, KafkaOrderEvent.class);
        send(orderEvent);
    }

    public abstract OperationType getOperation();

    protected void send(KafkaOrderEvent orderEvent) {
        populateMetaData(orderEvent);
        ListenableFuture<SendResult<String, KafkaOrderEvent>> result;
        final String errorMessage = "Unexpected error while sending: event={} to topic={} by key={}";
        try{
            result = orderEventTemplate.send(ORDER_TOPIC, UPDATE_ORDER_EVENT, orderEvent);
        } catch (Exception ex) {
            log.error(errorMessage, orderEvent, ORDER_TOPIC, UPDATE_ORDER_EVENT, ex);
            throw ex;
        }
        result.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(errorMessage, orderEvent, ORDER_TOPIC, UPDATE_ORDER_EVENT, ex);
            }

            @Override
            public void onSuccess(SendResult<String, KafkaOrderEvent> stringKafkaOrderEventSendResult) {
                log.info("Message sent successfully: event={}, topic={}, key={}",
                        orderEvent, ORDER_TOPIC, UPDATE_ORDER_EVENT);
            }
        });
    }

    private void populateMetaData(KafkaOrderEvent orderEvent) {
        orderEvent.setCreateAt(now());
        orderEvent.setCreateBy(ORDER_CLIENT_NAME);
        orderEvent.setOperationType(getOperation());
    }
}