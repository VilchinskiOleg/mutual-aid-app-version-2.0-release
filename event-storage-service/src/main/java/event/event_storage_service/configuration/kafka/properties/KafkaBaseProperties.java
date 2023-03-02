package event.event_storage_service.configuration.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaBaseProperties {

    private String bootstrapServers;
    private String groupId;
    private String listenTopics;
}