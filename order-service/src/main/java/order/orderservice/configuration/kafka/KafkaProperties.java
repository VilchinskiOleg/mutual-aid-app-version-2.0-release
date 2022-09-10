package order.orderservice.configuration.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaProperties {

    private String bootstrapServers;

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