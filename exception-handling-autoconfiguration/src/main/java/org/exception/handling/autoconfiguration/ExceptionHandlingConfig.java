package org.exception.handling.autoconfiguration;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.model.LocalizedErrorMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.exception.handling.autoconfiguration.handler")
@Slf4j
public class ExceptionHandlingConfig {

    public static final String RESOURCE_PATH = "localization-errors/error-messages.json";

    @Bean(name = "localizedErrorMessagesConfig")
    public Map<String, LocalizedErrorMessages> localizedErrorMessagesConfigInitialization() {
        Map<String, LocalizedErrorMessages> localizedErrorMessages;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            var resourceInputStream = ofNullable(classLoader.getResourceAsStream(RESOURCE_PATH))
                    .orElseThrow(() -> new IOException("Resource is NULL!"));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, LocalizedErrorMessages>> type = new TypeReference<>() {};
            localizedErrorMessages = objectMapper.readValue(resourceInputStream, type);
        } catch (IOException ex) {
            log.error("Error on starting app: cannot read error messages JSON file", ex);
            throw new RuntimeException("Error on starting app: cannot read error messages JSON file");
        }
        return localizedErrorMessages;
    }
}