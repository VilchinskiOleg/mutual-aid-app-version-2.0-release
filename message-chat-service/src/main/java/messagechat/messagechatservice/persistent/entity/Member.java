package messagechat.messagechatservice.persistent.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "member")

/**
 * Also you can use approach:
 *
 * @Table(name = "Term", indexes = {
 *     @Index(columnList = "name, id", name = "name_idx") })
 *
 * in order to define your custom index (not like unique single field or primary key, with will be created automatically)
 */
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            unique = true,
            nullable = false)
    private String profileId;

    @OneToOne(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false)
    private MemberInfo memberInfo;


    public Member(String profileId, MemberInfo memberInfo) {
        this.profileId = profileId;
        setMemberInfo(memberInfo);
    }

    public Member(Integer id) {
        this.id = id;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
        memberInfo.setMember(this);
    }
}