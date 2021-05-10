package messagechat.messagechatservice.domain.model;

import static lombok.EqualsAndHashCode.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {

    @Include
    private String memberId;
    private String firstName;
    private String lastName;
    private String nickName;
}
