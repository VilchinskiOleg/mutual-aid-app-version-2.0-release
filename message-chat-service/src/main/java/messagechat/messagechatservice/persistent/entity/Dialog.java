package messagechat.messagechatservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Document(collection = "message-chat-service.dialog")
public class Dialog {

    @Id
    private String id;

    private String internalId;
    private Set<Member> members;
    private String status;
    private String type;
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime modifyAt;
    private String createByMemberId;
    private String modifyByMemberId;
}