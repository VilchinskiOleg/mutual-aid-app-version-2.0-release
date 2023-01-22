package org.tms.profile_service_core.configuration.client;

import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tms.common.auth.configuration.client.AuthRestClientProperties;
import org.tms.profile_service_core.configuration.client.auth.AuthRestClientAuthInterceptor;

import javax.annotation.Resource;

/**
 * Can use that config class to create all needed Interceptor-Beans for different clients.
 */
@Configuration
public class FeignInterceptorsConfiguration {

    /**
     * That property bean we fetch from auth-configuration starter.
     */
    @Resource
    private AuthRestClientProperties authRestClientProperties;

    @Bean
    public AuthRestClientAuthInterceptor authRestClientAuthInterceptor(CommonData commonData) {
        return new AuthRestClientAuthInterceptor(
                authRestClientProperties.getUser(),
                authRestClientProperties.getPassword(),
                commonData
        );
    }
}