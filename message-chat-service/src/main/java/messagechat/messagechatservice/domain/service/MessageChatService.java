package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Message;
import org.springframework.data.domain.Page;

public interface MessageChatService {

    /**
     * User chooses certain particular message-chat (Dialog) and must be provided by all messages
     * (within received page parameters) from that Dialog.
     *
     * @param pageNumber
     * @param size
     * @param dialogId
     * @return Page<Message> - page of Messages.
     */
    Page<Message> getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId);

    Message addMessageToDialog(Message massage, String consumerId);

    Message updateMessage(Message massage);
}