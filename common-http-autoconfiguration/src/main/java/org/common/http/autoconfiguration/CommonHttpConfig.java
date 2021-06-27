package org.common.http.autoconfiguration;

import static org.common.http.autoconfiguration.utils.Constant.PROCESSED_URL_PATTERNS;

import org.common.http.autoconfiguration.interceptor.RequestCommonDataInterceptor;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class CommonHttpConfig implements WebMvcConfigurer {

    @Resource
    private CommonData commonData;

    @Bean(name = "commonData")
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CommonData createCommonData() {
        return new CommonData();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCommonDataInterceptor(commonData))
                .addPathPatterns(PROCESSED_URL_PATTERNS);
    }
}
