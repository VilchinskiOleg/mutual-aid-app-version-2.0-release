package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.model.page.PageMessages;

public interface MessageChatService {

    Message addMessageToDialog(Message massage, String dialogId);
    Message getMessageById(String massageId);
    PageMessages getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId);
    Dialog getDialogById(String dialogId);
}
