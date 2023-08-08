package messagechat.messagechatservice.domain.service.proessor.hibernate_listener;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.domain.service.proessor.ExternalCacheManager;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

@RequiredArgsConstructor
public class PreUpdateListener implements PreUpdateEventListener {

    private final ExternalCacheManager cacheManager;

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