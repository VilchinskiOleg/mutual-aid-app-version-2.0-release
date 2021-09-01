package org.tms.profile_service_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "org.tms.profile_service_rest.persistent")
public class ProfileServiceRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceRestApplication.class, args);
    }

}
