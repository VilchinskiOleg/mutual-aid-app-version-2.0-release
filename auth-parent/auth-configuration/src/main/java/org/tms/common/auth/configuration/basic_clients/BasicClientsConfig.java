package org.tms.common.auth.configuration.basic_clients;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class BasicClientsConfig {

  private static final String RESOURCE_PATH = "basicclients/basic-auth-clients.json";

  @Bean(name = "basicClients")
  public Map<String, BasicClient> getBasicClients() {

    Resource res = new ClassPathResource(RESOURCE_PATH);

    try {
      URL url = res.getURL();
      if (isNull(url)) {
        throw new RuntimeException(format("Error on starting app: cannot build URL for resource {}", RESOURCE_PATH));
      }
      ObjectMapper objectMapper = new ObjectMapper();
      TypeReference<List<BasicClient>> type = new TypeReference<>() {};
      var basicClients = objectMapper.readValue(new FileReader(url.getPath()), type);
      return basicClients.stream()
                         .collect(toMap(BasicClient::getName, client -> client));
    } catch (IOException ex) {
      throw new RuntimeException("Error on starting app: cannot read basic clients from JSON file");
    }
  }
}