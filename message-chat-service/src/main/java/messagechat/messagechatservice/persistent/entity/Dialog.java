package messagechat.messagechatservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "message-chat-service.dialog")
public class Dialog {

    @Id
    private String id;

    private String internalId;
    private List<Member> members;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
