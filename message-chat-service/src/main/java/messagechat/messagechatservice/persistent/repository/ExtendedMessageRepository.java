package messagechat.messagechatservice.persistent.repository;

import messagechat.messagechatservice.persistent.entity.Message;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ExtendedMessageRepository {

    /**
     * Just implementation with using Criteria api.
     *
     * @param page - object that contains info about position of messages were requested.
     * @param dialogId - ID of dialog which messages you want to get of.
     * @return page of Message.
     */
    List<Message> findAllByDialogId(PageRequest page, String dialogId);
}