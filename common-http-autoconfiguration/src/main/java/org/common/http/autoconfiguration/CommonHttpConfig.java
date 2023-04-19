package org.common.http.autoconfiguration;

import org.common.http.autoconfiguration.interceptor.RequestCommonDataInterceptor;
import org.common.http.autoconfiguration.model.CommonData;
import org.common.http.autoconfiguration.service.IdGeneratorService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

import static org.common.http.autoconfiguration.utils.Constant.PROCESSED_URL_PATTERNS;

@Configuration
@ComponentScan("org.common.http.autoconfiguration")
public class CommonHttpConfig implements WebMvcConfigurer {

    @Resource
    private CommonData commonData;
    @Resource
    private IdGeneratorService idGenerator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCommonDataInterceptor(commonData, idGenerator))
                .addPathPatterns(PROCESSED_URL_PATTERNS);
    }
}
