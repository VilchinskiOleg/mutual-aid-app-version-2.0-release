package messagechat.messagechatservice.domain.model;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

    public Member getAuthor() {
        if (isNull(author)) {
            author = new Member();
        }
        return author;
    }

    public String getAuthorId() {
        return getAuthor().getProfileId();
    }

    public String getAuthorNickName() {
        return getAuthor().getNickName();
    }

    public boolean isModified() {
        return nonNull(modifyAt);
    }
}