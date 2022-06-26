package org.tms.common.auth.configuration.basic_clients;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicClientsConfig {

  @Bean(name = "basicClients")
  public Map<String, BasicClient> getBasicClients() {
    URL url = ClassLoader.getSystemClassLoader()
        .getResource("basicclients/basic-auth-clients.json");
    if (isNull(url)) {
      throw new RuntimeException("Error on starting app: cannot build URL for resource 'basicclients/basic-auth-clients.json'");
    }
    Map<String, BasicClient> basicClientsByName;
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      TypeReference<Map<String, BasicClient>> type = new TypeReference<>() {};
      basicClientsByName = objectMapper.readValue(new FileReader(url.getPath()), type);
    } catch (IOException ex) {
      throw new RuntimeException("Error on starting app: cannot read basic clients from JSON file");
    }
    return basicClientsByName;
  }
}