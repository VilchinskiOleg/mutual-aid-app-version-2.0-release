package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Message;
import org.springframework.lang.Nullable;

import java.util.List;

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
    List<Message> getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId, @Nullable String dialogName);

    void addMessageToDialog(Message massage, String receiverId);

    Message updateMessage(Message massage);
}