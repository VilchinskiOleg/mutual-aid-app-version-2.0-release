package org.tms.authservicerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.tms.authservicerest.configuration.client")
@EnableMongoRepositories(basePackages = "org.tms.authservicerest.persistent")
public class AuthServiceRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceRestApplication.class, args);
  }

}
