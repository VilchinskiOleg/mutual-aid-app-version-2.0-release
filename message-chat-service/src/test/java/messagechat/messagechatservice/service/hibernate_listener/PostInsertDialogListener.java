package messagechat.messagechatservice.service.hibernate_listener;

import messagechat.messagechatservice.persistent.entity.Dialog;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

import static messagechat.messagechatservice.service.MessageChatServiceTest.DIALOG_ID;

public class PostInsertDialogListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        var entity = event.getEntity();
        if (Dialog.class.isAssignableFrom(entity.getClass())) {
            Dialog dialog = (Dialog) entity;
            DIALOG_ID = dialog.getDialogId();
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {return false;}
}