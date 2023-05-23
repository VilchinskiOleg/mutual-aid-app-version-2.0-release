package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

public interface MessageChatService {

    /**
     * User chooses certain particular message-chat (Dialog) and must be provided by all messages
     * (within received page parameters) from that Dialog.
     *
     * @param pageNumber
     * @param size
     * @param dialogId
     * @param dialogName - optional
     * @return Page<Message> - page of Messages.
     */
    Page<Message> getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId,@Nullable String dialogName);

    Message addMessageToDialog(Message massage, String receiverId);

    Message updateMessage(Message massage);
}