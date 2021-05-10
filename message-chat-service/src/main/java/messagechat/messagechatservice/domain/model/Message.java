package messagechat.messagechatservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

    private String id;
    private String dialogId;
    private String description;
    private Member author;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
