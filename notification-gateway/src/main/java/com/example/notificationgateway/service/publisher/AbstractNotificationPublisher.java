package com.example.notificationgateway.service.publisher;

import com.example.notificationgateway.model.NotificationContext;
import com.example.notificationgateway.model.NotificationContextConfig;
import com.example.notificationgateway.model.NotificationMessage;
import com.example.notificationgateway.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.tms.mutual_aid.profile_service.client.model.Contact;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
public abstract class AbstractNotificationPublisher implements NotificationPublisher {

    private final NavigableSet<NotificationPublisher> publishersChain;
    private final Map<NotificationKey, NotificationContextConfig> ctxConfigsByNotificationKey;
    private final ProfileService profileService;

    public AbstractNotificationPublisher(NavigableSet<NotificationPublisher> publishersChain,
                                         Map<NotificationKey, NotificationContextConfig> ctxConfigsByNotificationKey,
                                         ProfileService profileService) {
        this.publishersChain = publishersChain;
        this.ctxConfigsByNotificationKey = ctxConfigsByNotificationKey;
        this.profileService = profileService;
        publishersChain.add(this);
    }

    @Override
    public void sendNotification(NotificationMessage notificationMessage) {
        if (notificationMessage.getNotificationTypes().contains(getNotificationType())) {
            try {
                NotificationContext notificationContext = prepareNotificationContext(notificationMessage);
                doSendNotification(notificationContext);
            } catch (Exception e) {
                log.error("Wasn't able to execute {}. {}. This publisher was skipped",
                        this.getClass().getSimpleName(), e.getMessage());
            }
        }
        doChain(notificationMessage);
    }

    protected abstract void doSendNotification(NotificationContext notificationContext);

    protected abstract NotificationType getNotificationType();



    private NotificationContext prepareNotificationContext(NotificationMessage notificationMessage) {
        NotificationContextConfig ctxConfig = ctxConfigsByNotificationKey.get(notificationMessage.getNotificationKey());

        Map<NotificationType, String> templatePathByNotificationType = ctxConfig.getTemplatePathByNotificationType();
        String templatePath = templatePathByNotificationType.get(getNotificationType());

        Map<String, Object> notificationPayload = notificationMessage.getPayload();
        Map<String, Object> templateVariables = ctxConfig.getPayloadParams().stream()
                .map(key -> Map.entry(key, notificationPayload.get(key)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Contact> recipientContacts = profileService.getProfileContacts(notificationMessage.getRecipientId());
        Optional<Contact> suitableContact = recipientContacts.stream()
                .filter(contact -> contact.getType().contains(getNotificationType().toString().toLowerCase()))
                .findFirst();
        if (suitableContact.isEmpty()) {
            log.error("Wasn't able to find contact value inside user profile with profileId = {} for notification type = {}",
                    notificationMessage.getRecipientId(), getNotificationType());
            throw new IllegalArgumentException("Wasn't able to find contact value inside user profile for appropriate notification type");
        }

        return new NotificationContext(templatePath, templateVariables, suitableContact.get().getValue());
    }

    /**
     * Delegates processing to next Publisher
     *
     * @param notificationMessage - {@link NotificationMessage} provided by client
     */
    private void doChain(NotificationMessage notificationMessage) {
         var next = publishersChain.higher(this); // Returns next Publisher, after the current
         if (nonNull(next)) {
             next.sendNotification(notificationMessage);
         }
    }



    public enum NotificationKey {

        RESET_WEEK_PASSWORD, RESET_PASSWORD;
    }

    public enum NotificationType {

        MAIL, PHONE;
    }

    @Override
    public int compareTo(NotificationPublisher o) {
        if (o instanceof AbstractNotificationPublisher) {
            AbstractNotificationPublisher objToCompareWith = (AbstractNotificationPublisher) o;
            NotificationType curInstType = Objects.requireNonNull(this.getNotificationType());
            NotificationType instToCompareWithType = Objects.requireNonNull(objToCompareWith.getNotificationType());
            return curInstType.compareTo(instToCompareWithType);
        } else {
            throw new IllegalArgumentException("Argument must extend extend AbstractNotificationPublisher.class");
        }
    }
}
