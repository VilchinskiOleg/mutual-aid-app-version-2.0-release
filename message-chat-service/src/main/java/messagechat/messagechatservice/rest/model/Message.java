package messagechat.messagechatservice.rest.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

    private String id;

    private String dialogId;
    private String description;

    private String authorId;
    private String authorNickName;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private Boolean isModified;
}