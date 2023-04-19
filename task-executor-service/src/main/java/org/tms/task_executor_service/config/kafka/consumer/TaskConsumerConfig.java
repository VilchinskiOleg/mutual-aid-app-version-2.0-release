package org.tms.task_executor_service.config.kafka.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;
import org.tms.common.kafka.avro.TaskEvent;

import java.util.HashMap;
import java.util.Map;

@Component
public class TaskConsumerConfig {

    /**
     * Properties.
     *
     * Default values:
     * MAX_POLL_INTERVAL_MS = 300000
     * MAX_POLL_RECORDS     = 500
     * AUTO_OFFSET_RESET_CONFIG = 'latest'
     */
    public static final Integer MAX_POLL_INTERVAL_MS = 3000000;
    public static final Integer MAX_POLL_RECORDS = 100;
    public static final String LATEST = "latest";
    public static final String EARLIEST = "earliest";

    @Bean
    public ConsumerFactory<String, TaskEvent> consumerFactory(
            @Value("${kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${kafka.schema-registry-url}") final String schemaRegistryUrl) {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "task-listener-group");

        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, LATEST);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("auto.register.schemas", false);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskEvent> taskEventListenerContainerFactory(
            ConsumerFactory<String, TaskEvent> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, TaskEvent>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}