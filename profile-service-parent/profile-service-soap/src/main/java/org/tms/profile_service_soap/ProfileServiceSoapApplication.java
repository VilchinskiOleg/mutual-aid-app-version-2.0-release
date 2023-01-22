package org.tms.profile_service_soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.tms.profile_service_core.ProfileCoreConfig;

@SpringBootApplication
@Import(ProfileCoreConfig.class)
public class ProfileServiceSoapApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceSoapApplication.class, args);
    }

}