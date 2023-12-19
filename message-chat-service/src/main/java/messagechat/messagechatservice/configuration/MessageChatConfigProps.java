package messagechat.messagechatservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Wrap message chat properties and/or process them.
 */
@Component
@Getter
public class MessageChatConfigProps {

    @Value("${jpa-props.message-chat.ddl-auto}")
    private String ddlAuto;

    @Value("${message-chat-properties.translation-message.enabled}")
    private boolean translationMessageEnabled;

    @Value("${redis-connection.host}")
    private String redisHost;
    @Value("${redis-connection.port}")
    private int redisPort;


    public boolean isTranslationEnabled() {
        return translationMessageEnabled;
    }
}