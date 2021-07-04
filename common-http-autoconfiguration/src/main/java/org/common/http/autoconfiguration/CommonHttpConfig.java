package org.common.http.autoconfiguration;

import static org.common.http.autoconfiguration.utils.Constant.PROCESSED_URL_PATTERNS;

import org.common.http.autoconfiguration.interceptor.RequestCommonDataInterceptor;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
@ComponentScan("org.common.http.autoconfiguration")
public class CommonHttpConfig implements WebMvcConfigurer {

    @Resource
    private CommonData commonData;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCommonDataInterceptor(commonData))
                .addPathPatterns(PROCESSED_URL_PATTERNS);
    }
}
