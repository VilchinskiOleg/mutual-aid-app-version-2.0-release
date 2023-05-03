package messagechat.messagechatservice.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Member {

    private Integer id;
    @Include
    private String profileId;

    private String firstName;
    private String lastName;
    private String nickName;

    public Member(String profileId) {
        this.profileId = profileId;
    }
}