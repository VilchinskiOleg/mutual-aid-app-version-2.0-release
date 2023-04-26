package messagechat.messagechatservice.persistent.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "dialog_by_member")
public class DialogByMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime memberJoinedToDialogAt;


    public DialogByMember(Dialog dialog, Member member) {
        this.dialog = dialog;
        this.member = member;
        this.memberJoinedToDialogAt = LocalDateTime.now();
    }
}