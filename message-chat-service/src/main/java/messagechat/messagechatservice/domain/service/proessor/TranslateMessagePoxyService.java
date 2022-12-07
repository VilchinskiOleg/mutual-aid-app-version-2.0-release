package messagechat.messagechatservice.domain.service.proessor;

import messagechat.messagechatservice.configuration.MessageChatConfig;
import messagechat.messagechatservice.domain.model.Message;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

@Component
public class TranslateMessagePoxyService extends TranslateMessageService {

    @Resource
    private MessageChatConfig messageChatConfig;

    @Override
    public void translateSavedMessage(Message message) {
        if (messageChatConfig.isTranslationEnabled()) {
            super.translateSavedMessage(message);
        }
    }

    @Override
    public void translateReturnedMessages(List<Message> messages) {
        if (messageChatConfig.isTranslationEnabled()) {
            super.translateReturnedMessages(messages);
        }
    }
}