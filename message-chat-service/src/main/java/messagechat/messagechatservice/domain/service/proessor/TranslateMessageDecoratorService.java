package messagechat.messagechatservice.domain.service.proessor;

import messagechat.messagechatservice.configuration.MessageChatConfig;
import messagechat.messagechatservice.domain.model.Message;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class TranslateMessageDecoratorService extends TranslateMessageService {

    @Resource
    private MessageChatConfig messageChatConfig;

    @Override
    public void translateMessageForSave(Message message) {
        if (messageChatConfig.isTranslationEnabled()) {
            super.translateMessageForSave(message);
        }
    }

    @Override
    public void translateMessageForReturn(Message message) {
        if (messageChatConfig.isTranslationEnabled()) {
            super.translateMessageForReturn(message);
        }
    }
}