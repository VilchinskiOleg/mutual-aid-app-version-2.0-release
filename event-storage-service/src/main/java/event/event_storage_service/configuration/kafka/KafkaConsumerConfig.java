package event.event_storage_service.configuration.kafka;

import static event.event_storage_service.util.Constant.Kafka.*;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.configuration.kafka.deserializer.KafkaOrderEventDeserializer;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Resource
    private KafkaProperties properties;
    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public ConsumerFactory<String, KafkaOrderEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());

        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, LATEST);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaOrderEventDeserializer(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaOrderEvent> orderEventListenerContainerFactory(ConsumerFactory<String, KafkaOrderEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaOrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(record -> isFalse(UPDATE_ORDER_EVENT.equals(record.key()))); //defined excluded values.
        return factory;
    }
}