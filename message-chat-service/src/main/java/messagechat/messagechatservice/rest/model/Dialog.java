package messagechat.messagechatservice.rest.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class Dialog {

    private String dialogId;
    private Set<String> memberIds;
    private String status;
    private String type;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private String createByMemberId;
    private String modifyByMemberId;
}