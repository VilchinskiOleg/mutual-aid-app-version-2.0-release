package org.tms.profile_service_rest.configuration.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Allowed values for serializationInclusion(...) -> always, non_null, non_absent, non_default, non_empty
 *
 * Also you can define that behavior by declarative approach (use application.yml):
 * spring.jackson.default-property-inclusion=always, non_null, non_absent, non_default, non_empty
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
//                .serializers(LOCAL_DATETIME_SERIALIZER)
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}