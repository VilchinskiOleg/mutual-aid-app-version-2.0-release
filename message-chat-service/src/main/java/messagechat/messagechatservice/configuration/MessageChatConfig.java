package messagechat.messagechatservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageChatConfig {

    @Value("${message-chat-properties.translation-message.enabled}")
    private boolean translationMessageEnabled;

    public boolean isTranslationEnabled() {
        return translationMessageEnabled;
    }
}