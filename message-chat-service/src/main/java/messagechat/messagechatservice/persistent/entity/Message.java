package messagechat.messagechatservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "message-chat-service.message")
public class Message {

    @Id
    private String id;

    private String dialogId;
    private String description;
    private Member author;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}