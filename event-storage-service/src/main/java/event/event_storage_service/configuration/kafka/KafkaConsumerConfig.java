package event.event_storage_service.configuration.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.configuration.kafka.deserializer.KafkaOrderEventDeserializer;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.configuration.kafka.properties.KafkaBaseProperties;
import event.event_storage_service.configuration.kafka.properties.KafkaSSLProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static event.event_storage_service.util.Constant.Kafka.*;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Configuration
public class KafkaConsumerConfig {

    @Resource
    private KafkaBaseProperties properties;
    @Autowired(required = false)
    private KafkaSSLProperties sslProperties;
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

        if (nonNull(sslProperties)) {
            props.put("security.protocol", "SSL");
            props.put("ssl.client.auth", "required");
            props.put("ssl.enabled.protocols", "TLSv1,TLSv1.2,TLSv1.1,TLSv1.3");
            props.put("ssl.key.password", sslProperties.getSslKeyPassword());
            props.put("ssl.keystore.location", sslProperties.getSslKeystoreLocation());
            props.put("ssl.keystore.password", sslProperties.getSslKeystorePassword());
            props.put("ssl.truststore.location", sslProperties.getSslTruststoreLocation());
            props.put("ssl.truststore.password", sslProperties.getSslTruststorePassword());
        }

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