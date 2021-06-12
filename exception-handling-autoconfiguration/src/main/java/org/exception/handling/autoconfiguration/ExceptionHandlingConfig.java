package org.exception.handling.autoconfiguration;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.exception.handling.autoconfiguration.model.LocalizedErrorMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.exception.handling.autoconfiguration.handler")
public class ExceptionHandlingConfig {

    @Bean
    @Nullable
    public Map<String, LocalizedErrorMessages> localizedErrorMessagesConfig() {
        URL url = ClassLoader.getSystemClassLoader()
                .getResource("localization-errors/error-messages.json");
        if (isNull(url)) {
            throw new RuntimeException("Error on starting app: cannot build URL for resource 'localization-errors/error-messages.json'");
        }
        Map<String, LocalizedErrorMessages> localizedErrorMessages;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, LocalizedErrorMessages>> type = new TypeReference<Map<String, LocalizedErrorMessages>>() {};
            localizedErrorMessages = objectMapper.readValue(new FileReader(url.getPath()), type);
        } catch (IOException ex) {
            throw new RuntimeException("Error on starting app: cannot read error messages JSON file");
        }
        return localizedErrorMessages;
    }
}
