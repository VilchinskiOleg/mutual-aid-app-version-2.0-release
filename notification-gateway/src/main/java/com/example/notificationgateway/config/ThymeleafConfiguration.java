package com.example.notificationgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    public TemplateEngine emailTemplateEngine() {
        var templateEngine = new SpringTemplateEngine();

        // Resolver for HTML emails (except the editable one) :
        templateEngine.addTemplateResolver(htmlTemplateResolver());

        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver()  {
        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setPrefix("templates/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
