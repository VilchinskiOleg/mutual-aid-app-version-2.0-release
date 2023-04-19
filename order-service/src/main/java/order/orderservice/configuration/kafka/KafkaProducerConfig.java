package order.orderservice.configuration.kafka;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.configuration.kafka.properties.KafkaBaseProperties;
import order.orderservice.configuration.kafka.properties.KafkaSSLProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Configuration
public class KafkaProducerConfig {

    @Resource
    private KafkaBaseProperties baseProperties;
    @Autowired(required = false)
    private KafkaSSLProperties sslProperties;

    @Bean
    public ProducerFactory<String, KafkaOrderEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, baseProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

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

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, KafkaOrderEvent> orderEventTemplate(ProducerFactory<String, KafkaOrderEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}