package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.model.page.PageMessages;

public interface MessageChatService {

    Message addMessage(Message massage, String dialogId);
    Message getMessageById(String dialogId, String massageId);
    PageMessages getNextPackMessageFromDialog(Integer page, Integer size, String dialogId);
    Dialog getDialogById(String dialogId);
}
