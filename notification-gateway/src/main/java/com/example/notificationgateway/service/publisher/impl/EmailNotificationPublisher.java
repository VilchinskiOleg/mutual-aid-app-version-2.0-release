package com.example.notificationgateway.service.publisher.impl;


import com.example.notificationgateway.model.NotificationContextConfig;
import com.example.notificationgateway.model.NotificationContext;
import com.example.notificationgateway.service.ProfileService;
import com.example.notificationgateway.service.publisher.AbstractNotificationPublisher;
import com.example.notificationgateway.service.publisher.NotificationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.NavigableSet;

@Slf4j
@Component
public class EmailNotificationPublisher extends AbstractNotificationPublisher {

    private static final String SENDER_EMAIL = "vin76423@gmail.com";

    private static final String SUBJECT = "Congratulation! You won!"; //todo

    private final JavaMailSender emailSender; // Uses 'spring.mail' properties for configuring ??
    private final TemplateEngine templateEngine;

    public EmailNotificationPublisher(@Qualifier("publishersChain") NavigableSet<NotificationPublisher> publishersChain, ProfileService profileService,
                                      Map<NotificationKey, NotificationContextConfig> cxtConfigsByNotificationKey,
                                      JavaMailSender emailSender, TemplateEngine templateEngine) {
        super(publishersChain, cxtConfigsByNotificationKey, profileService);
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void doSendNotification(NotificationContext ctx) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(SENDER_EMAIL);
            helper.setTo(ctx.getRecipientAddress());
            helper.setSubject(SUBJECT); // todo

            String renderedHtml = renderContent(ctx);
            helper.setText(renderedHtml, true);

            emailSender.send(message);
        } catch (Exception ex) {
            log.error("Unexpected error during sending notification to user mail = {}", ctx.getRecipientAddress(), ex);
        }
    }

    @Override
    protected NotificationType getNotificationType() {
        return NotificationType.MAIL;
    }

    private String renderContent(NotificationContext notificationCtx) {
        Context engineCtx = new Context();
        engineCtx.setVariables(notificationCtx.getTemplateVariables());
        return templateEngine.process(notificationCtx.getMessageTemplatePath(), engineCtx);
    }
}
