package messagechat.messagechatservice.domain.model;

import lombok.*;

import static lombok.EqualsAndHashCode.Include;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    // ID in DB:
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