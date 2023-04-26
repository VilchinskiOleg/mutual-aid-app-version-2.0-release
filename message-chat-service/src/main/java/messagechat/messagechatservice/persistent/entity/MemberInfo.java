package messagechat.messagechatservice.persistent.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "member_info")
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
//    @PrimaryKeyJoinColumn
    @JoinColumn(name = "id")
    private Member member;

    private String firstName;
    private String lastName;
    private String nickName;
}