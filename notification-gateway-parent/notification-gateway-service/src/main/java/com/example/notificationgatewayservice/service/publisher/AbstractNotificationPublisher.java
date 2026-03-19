package com.example.notificationgatewayservice.service.publisher;

import com.example.notificationconfig.model.NotificationContextConfig;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationKey;
import com.example.notificationconfig.model.NotificationContextConfig.NotificationType;
import com.example.notificationconfig.model.NotificationMessage;
import com.example.notificationgatewayservice.model.NotificationContext;
import com.example.notificationgatewayservice.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.tms.mutual_aid.profile_service.client.model.Contact;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

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

    /**
     * Converts {@link NotificationMessage} to specific {@link NotificationContext} according to
     * the {@link NotificationKey} and {@link NotificationType}.
     *
     * @param notificationMessage
     * @return
     */
    private NotificationContext prepareNotificationContext(NotificationMessage notificationMessage) {
        NotificationContextConfig ctxConfig = ctxConfigsByNotificationKey.get(notificationMessage.getNotificationKey());
        return NotificationContext.builder()
                .title(getTitle(notificationMessage.getNotificationKey()))
                .messageTemplatePath(getTemplateFileName(ctxConfig))
                .templateVariables(getTemplateVariables(notificationMessage, ctxConfig))
                .recipientAddress(getRecipientAddress(notificationMessage))
                .build();
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

    private String getTitle(NotificationKey notificationKey) {
        var words = Arrays.stream(notificationKey.toString().split("_"))
                .map(str -> StringUtils.capitalize(str.toLowerCase()))
                .collect(Collectors.toList());
        return String.join(" ", words);
    }

    private String getTemplateFileName(NotificationContextConfig ctxConfig) {
        Map<NotificationType, String> templatePathByNotificationType = ctxConfig.getTemplatePathByNotificationType();
        return templatePathByNotificationType.get(getNotificationType());
    }

    private Map<String, Object> getTemplateVariables(NotificationMessage notificationMessage,
                                                     NotificationContextConfig ctxConfig) {
        Map<String, Object> notificationPayload = notificationMessage.getPayload();
        // Fetch variables from payload, which related only to current template
        // (because some of them in payload can be related to another template and NotificationType) :
        return ctxConfig.getPayloadParams().stream()
            .map(key -> Map.entry(key, notificationPayload.get(key)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getRecipientAddress(NotificationMessage notificationMessage) {
        List<Contact> recipientContacts = profileService.getProfileContacts(notificationMessage.getRecipientId());
        Optional<Contact> suitableContact = recipientContacts.stream()
                .filter(contact -> contact.getType().toUpperCase().contains(
                        getNotificationType().toString().toUpperCase()))
                .findFirst();
        if (suitableContact.isEmpty()) {
            log.error("Wasn't able to find contact value inside user profile with profileId = {} for notification type = {}",
                    notificationMessage.getRecipientId(), getNotificationType());
            throw new IllegalArgumentException("Wasn't able to find contact value inside user profile for appropriate notification type");
        }
        return suitableContact.get().getValue();
    }

    @Override
    public int compareTo(NotificationPublisher o) {
        if (o instanceof AbstractNotificationPublisher) {
            AbstractNotificationPublisher objToCompareWith = (AbstractNotificationPublisher) o;
            NotificationType curInstType = requireNonNull(this.getNotificationType());
            NotificationType instToCompareWithType = requireNonNull(objToCompareWith.getNotificationType());
            return curInstType.compareTo(instToCompareWithType);
        } else {
            throw new IllegalArgumentException("Argument must extend extend AbstractNotificationPublisher.class");
        }
    }
}
