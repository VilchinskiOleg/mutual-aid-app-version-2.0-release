package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ExtendedMessageRepository {

    Page<Message> findAllByDialogId(PageRequest pageRequest, String dialogId);
}
