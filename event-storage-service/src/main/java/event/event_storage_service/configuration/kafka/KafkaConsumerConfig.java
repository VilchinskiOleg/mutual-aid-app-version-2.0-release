package event.event_storage_service.configuration.kafka;

import static event.event_storage_service.util.Constant.Kafka.MAX_POLL_INTERVAL_MS;
import static event.event_storage_service.util.Constant.Kafka.MAX_POLL_RECORDS;

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
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaOrderEventDeserializer(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaOrderEvent> kafkaListenerContainerFactory(ConsumerFactory<String, KafkaOrderEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaOrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
