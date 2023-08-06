package messagechat.messagechatservice.mapper.response;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.rest.message.response.MessagesPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class MessagesPageToMessagesPageResponseConverter extends PageToPageResponseConverter<Message, MessagesPageResponse> {

    @Override
    public void convert(Page<Message> source, MessagesPageResponse destination) {
        destination.setMessages(mapper.map(source.getContent(),
                                           new ArrayList<>(),
                                           messagechat.messagechatservice.rest.model.Message.class));
        super.convert(source, destination);
    }
}