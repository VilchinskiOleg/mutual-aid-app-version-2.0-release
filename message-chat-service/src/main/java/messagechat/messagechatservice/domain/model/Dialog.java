package messagechat.messagechatservice.domain.model;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Dialog {

    private String id;

    private String dialogId;
    private List<Member> members;
    private Status status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public enum Status {
        OPEN,
        CLOSE
    }

    public boolean hasMember(Member member) {
        if (isEmpty(members)) {
            return false;
        }
        return members.contains(member);
    }

    public void addMember(Member member) {
        if (isEmpty(members)) {
            members = new ArrayList<>();
        }
        if (nonNull(member)) {
            members.add(member);
        }
    }
}
