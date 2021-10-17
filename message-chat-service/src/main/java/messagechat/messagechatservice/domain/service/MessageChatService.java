package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Message;
import org.springframework.data.domain.Page;

public interface MessageChatService {

    Message addMessageToDialog(Message massage, String consumerId);

    Message updateMessage(Message massage);

    Page<Message> getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId);
}