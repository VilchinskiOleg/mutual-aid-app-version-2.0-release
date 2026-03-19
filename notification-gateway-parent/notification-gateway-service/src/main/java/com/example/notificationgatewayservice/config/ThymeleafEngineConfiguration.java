package com.example.notificationgatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafEngineConfiguration {

    @Bean
    @Primary
    public TemplateEngine thymeleafTemplateEngine() {
        var templateEngine = new SpringTemplateEngine();

        // Resolver for HTML emails (except the editable one) :
        templateEngine.addTemplateResolver(htmlTemplateResolver());

        return templateEngine;
    }

    public ITemplateResolver htmlTemplateResolver()  {
        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(false);
        templateResolver.setOrder(1);
        return templateResolver;
    }
}
