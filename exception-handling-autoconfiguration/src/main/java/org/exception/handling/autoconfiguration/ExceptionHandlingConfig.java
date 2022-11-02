package org.exception.handling.autoconfiguration;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.model.LocalizedErrorMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.exception.handling.autoconfiguration.handler")
@Slf4j
public class ExceptionHandlingConfig {

    @Bean(name = "localizedErrorMessagesConfig")
    public Map<String, LocalizedErrorMessages> localizedErrorMessagesConfigInitialization() {
        URL url = ClassLoader.getSystemClassLoader()
                .getResource("localization-errors/error-messages.json");
        if (isNull(url)) {
            throw new RuntimeException("Error on starting app: cannot build URL for resource 'localization-errors/error-messages.json'");
        }
        Map<String, LocalizedErrorMessages> localizedErrorMessages;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, LocalizedErrorMessages>> type = new TypeReference<>() {};
            localizedErrorMessages = objectMapper.readValue(new FileReader(url.getPath()), type);
        } catch (IOException ex) {
            log.error("Error on starting app: cannot read error messages JSON file", ex);
            throw new RuntimeException("Error on starting app: cannot read error messages JSON file");
        }
        return localizedErrorMessages;
    }
}
