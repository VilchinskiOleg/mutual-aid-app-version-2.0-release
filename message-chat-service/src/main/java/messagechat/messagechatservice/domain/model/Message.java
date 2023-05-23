package messagechat.messagechatservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    // ID in DB:
    private Integer id;
    private String internalId;

    private String description;
    private Member author;
    private Dialog dialog;
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

    public String getDialogId() {
        return isNull(dialog) ? null : dialog.getInternalId();
    }
}