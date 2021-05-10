package messagechat.messagechatservice.rest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShirtMessage {

    private String id;
    private String dialogId;

    private String description;
    private String author;
}
