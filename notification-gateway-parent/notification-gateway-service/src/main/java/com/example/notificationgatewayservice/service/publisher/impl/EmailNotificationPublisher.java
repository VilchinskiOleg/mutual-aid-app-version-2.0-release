package com.example.notificationgatewayservice.service.publisher.impl;

import com.example.notificationconfig.model.NotificationContextConfig;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import com.example.notificationgatewayservice.model.NotificationContext;
import com.example.notificationgatewayservice.service.ProfileService;
import com.example.notificationgatewayservice.service.publisher.AbstractNotificationPublisher;
import com.example.notificationgatewayservice.service.publisher.NotificationPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.NavigableSet;

@Slf4j
@Component
public class EmailNotificationPublisher extends AbstractNotificationPublisher {

    @Value("${notification-gateway.publishers.mail.sender-mail-address}")
    private String senderMailAddress;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public EmailNotificationPublisher(@Qualifier("publishersChain") NavigableSet<NotificationPublisher> publishersChain,
                                      ProfileService profileService,
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

            helper.setFrom(senderMailAddress);
            helper.setTo(ctx.getRecipientAddress());
            helper.setSubject(ctx.getTitle());

            String renderedHtml = renderContent(ctx);
            helper.setText(renderedHtml, true);

            emailSender.send(message);
        } catch (MailException | MessagingException ex) {
            log.error("Unexpected error during sending notification via MAIL to recipient address = {}",
                    ctx.getRecipientAddress(), ex);
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
