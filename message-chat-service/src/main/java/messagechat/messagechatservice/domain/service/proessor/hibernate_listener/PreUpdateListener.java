package messagechat.messagechatservice.domain.service.proessor.hibernate_listener;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

@RequiredArgsConstructor
public class PreUpdateListener implements PreUpdateEventListener {

    private final CacheManagerImpl cacheManager;

    /**
     * Remove Cached Messages by MessageId if that Message is gonna be updated.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        var entity = event.getEntity();
        if (Message.class.isAssignableFrom(entity.getClass())) {
            Message message = (Message) entity;
            cacheManager.removeMessageByMessageId(message.getMessageId());
        }
        return false;
    }
}