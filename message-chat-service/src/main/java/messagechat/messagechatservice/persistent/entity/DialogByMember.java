package messagechat.messagechatservice.persistent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "dialog_by_member")
public class DialogByMember {

    @EmbeddedId
    private DialogByMemberKey id = new DialogByMemberKey();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("dialogId")
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime memberJoinedToDialogAt;


    public DialogByMember(Dialog dialog, Member member) {
        this.dialog = dialog;
        this.member = member;
        if (nonNull(dialog.getId()) && nonNull(member.getId())) {
            this.id.setDialogId(dialog.getId());
            this.id.setMemberId(member.getId());
        }
        this.memberJoinedToDialogAt = LocalDateTime.now();
    }
}