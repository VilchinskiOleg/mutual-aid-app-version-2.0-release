package messagechat.messagechatservice.domain.service.proessor.hibernate_listener;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.entity.Message;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;

@RequiredArgsConstructor
public class PreInsertListener implements PreInsertEventListener {

    private final CacheManagerImpl cacheManager;

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        var entity = event.getEntity();
        if (Message.class.isAssignableFrom(entity.getClass())) {
            // Order of messages in a cache will be destroyed, so that we have to clean cache for this dialog:
            Message message = (Message) entity;
            String dialogId = message.getDialog().getDialogId();
            cacheManager.removeAllMessagesByDialogId(dialogId);
        }
        return false;
    }
}