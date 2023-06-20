package messagechat.messagechatservice.persistent.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode

@Getter
@Setter
public class DialogByMemberKey implements Serializable {

    @Column(name = "dialog_id")
    private Integer dialogId;
    @Column(name = "member_id")
    private Integer memberId;
}