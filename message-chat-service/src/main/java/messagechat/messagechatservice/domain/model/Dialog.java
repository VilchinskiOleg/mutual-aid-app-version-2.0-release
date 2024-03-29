package messagechat.messagechatservice.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dialog {

    // ID in DB:
    private Integer id;
    private Long version;

    private String internalId;

    // Name of Group if there are more than two members, or default generated name (like: member1_member2):
    private String name;

    private Set<Member> members;
    private Status status;
    private Type type;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private String createByMemberId;
    private String modifyByMemberId;

    public Set<Member> getMembers() {
        if (isNull(members)) {
            members = new HashSet<>();
        }
        return members;
    }

    public boolean hasMember(String memberId) {
        return getMembers().stream()
                           .anyMatch(member -> member.getProfileId().equals(memberId));
    }

    public boolean hasNotMember(String memberId) {
        return isFalse(hasMember(memberId));
    }

    public void addMember(Member member) {
        if (nonNull(member)) {
            getMembers().add(member);
        }
    }

    public void removeMember(Member member) {
        if (nonNull(member)) {
            getMembers().remove(member);
        }
    }

    public Member getMemberById(String memberId) {
        return getMembers().stream()
                           .filter(member -> member.getProfileId().equals(memberId))
                           .findFirst()
                           .orElse(null);
    }

    public enum Status {
        ACTIVE,
        NOT_ACTIVE
    }

    public enum Type {

        /**
         * Can have as meany as you want members.
         */
        CHANNEL,

        /**
         * Must have only two members.
         */
        FACE_TO_FACE_DIALOG
    }
}