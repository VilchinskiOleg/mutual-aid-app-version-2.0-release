package org.tms.profile_service_core;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan("org.tms.profile_service_core")
@EnableMongoRepositories(basePackages = "org.tms.profile_service_core.persistent")
@EnableFeignClients(basePackages = "org.tms.profile_service_core.configuration.client")
public class ProfileCoreConfig { }