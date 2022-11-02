package event.event_storage_service.configuration.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class KafkaOrderEventDeserializer implements Deserializer<KafkaOrderEvent> {

    private final ObjectMapper objectMapper;

    public KafkaOrderEventDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No-op
    }

    @Override
    public void close() {
        // No-op
    }

    @Override
    public KafkaOrderEvent deserialize(String s, byte[] bytes) {
        return deserializeMessage(bytes);
    }

    @Override
    public KafkaOrderEvent deserialize(String topic, Headers headers, byte[] data) {
        return deserializeMessage(data);
    }

    private KafkaOrderEvent deserializeMessage(byte[] data) {
        try {
            KafkaOrderEvent orderEvent = objectMapper.readValue(data, KafkaOrderEvent.class);
            return orderEvent;
        } catch (IOException ex) {
            log.error("Unexpected error. Error deserializing message: ", ex);
        }
        return null;
    }
}
