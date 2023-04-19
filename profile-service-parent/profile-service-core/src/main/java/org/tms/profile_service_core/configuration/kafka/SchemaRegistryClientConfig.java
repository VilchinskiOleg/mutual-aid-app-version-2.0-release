package org.tms.profile_service_core.configuration.kafka;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaRegistryClientConfig {

    @Bean
    public SchemaRegistryClient schemaRegistryClient(
            @Value("${kafka.schema-registry-url}") final String schemaRegistryUrl) {
        return new CachedSchemaRegistryClient(schemaRegistryUrl, 10);
    }
}