package org.tms.profile_service_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.tms.profile_service_core.ProfileCoreConfig;

@SpringBootApplication
@Import(ProfileCoreConfig.class)
public class ProfileServiceRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceRestApplication.class, args);
    }
}