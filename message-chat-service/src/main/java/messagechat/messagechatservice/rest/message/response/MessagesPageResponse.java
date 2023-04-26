package messagechat.messagechatservice.rest.message.response;

import lombok.*;
import messagechat.messagechatservice.rest.model.Message;
import java.util.List;

@Getter
@Setter
public class MessagesPageResponse extends PageResponse {

    private String dialogId;
    private String dialogName;
    private List<Message> messages;
}