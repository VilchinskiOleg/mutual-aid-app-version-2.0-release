package event.event_storage_service.configuration.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
@Profile(value = {"qa, prod"})
public class KafkaSSLProperties {

    @Value("ssl-key-password")
    private String sslKeyPassword;
    @Value("ssl-keystore-location")
    private String sslKeystoreLocation;
    @Value("ssl-keystore-password")
    private String sslKeystorePassword;
    @Value("ssl-truststore-location")
    private String sslTruststoreLocation;
    @Value("ssl-truststore-password")
    private String sslTruststorePassword;
}