package org.tms.common.auth.configuration.basic_clients;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicClientsConfig {

  @Bean(name = "basicClients")
  @ConfigurationProperties(prefix = "basic-clients")
  public Map<String, BasicClient> getBasicClients(List<BasicClient> basicClients) {
    //if empty or null?
    return basicClients.stream()
                       .collect(toMap(BasicClient::getName, client -> client));
  }
}