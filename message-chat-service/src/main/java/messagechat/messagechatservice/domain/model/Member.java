package messagechat.messagechatservice.domain.model;

import static lombok.EqualsAndHashCode.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Member {

    @Include
    private String profileId;
    private String firstName;
    private String lastName;
    private String nickName;

    public Member(String profileId) {
        this.profileId = profileId;
    }
}