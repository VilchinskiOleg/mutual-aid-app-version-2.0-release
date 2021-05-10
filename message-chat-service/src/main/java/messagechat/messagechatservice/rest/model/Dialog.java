package messagechat.messagechatservice.rest.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Dialog {

    private String dialogId;
    private List<Member> members;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
