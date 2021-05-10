package messagechat.messagechatservice.persistent.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private String memberId;
    private String firstName;
    private String lastName;
    private String nickName;
}
