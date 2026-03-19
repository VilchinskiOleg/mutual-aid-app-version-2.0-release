package org.tms.authservicerest;

import com.example.notificationconfig.config.NotificationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@Import(NotificationConfiguration.class)
@EnableMongoRepositories(basePackages = "org.tms.authservicerest.persistent")
public class AuthServiceRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthServiceRestApplication.class, args);
  }
}