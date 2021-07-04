package org.common.http.autoconfiguration;

import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class CommonDataConfig {

    @Bean(name = "commonData")
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CommonData createCommonData() {
        return new CommonData();
    }
}
