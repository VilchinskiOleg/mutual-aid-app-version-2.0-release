package org.tms.common.auth.configuration.basic_clients;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BasicClientsConfig {

  private static final String RESOURCE_PATH = "basicclients/basic-auth-clients.json";

  @Bean(name = "basicClients")
  public Map<String, BasicClient> getBasicClients() {
    try {
      var resourceAsInputStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH);
      if (isNull(resourceAsInputStream)) {
        log.error("Cannot read resource by path = {}", RESOURCE_PATH);
        throw new IOException(format("Cannot read resource by path = {}", RESOURCE_PATH));
      }
      ObjectMapper objectMapper = new ObjectMapper();
      TypeReference<List<BasicClient>> type = new TypeReference<>() {};
      var basicClients = objectMapper.readValue(resourceAsInputStream, type);
      return basicClients.stream()
                         .collect(toMap(BasicClient::getName, client -> client));
    } catch (IOException ex) {
      throw new RuntimeException("Error on starting app: cannot read basic clients from JSON file");
    }
  }
}