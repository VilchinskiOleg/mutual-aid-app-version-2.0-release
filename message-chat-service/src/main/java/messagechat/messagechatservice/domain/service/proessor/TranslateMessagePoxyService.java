package messagechat.messagechatservice.domain.service.proessor;

import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.domain.model.Message;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

@Component
public class TranslateMessagePoxyService extends TranslateMessageService {

    @Resource
    private MessageChatConfigProps messageChatConfigProps;

    @Override
    public void translateReturnedMessages(List<Message> messages) {
        if (messageChatConfigProps.isTranslationEnabled()) {
            super.translateReturnedMessages(messages);
        }
    }
}