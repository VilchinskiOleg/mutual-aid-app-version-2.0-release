package order.orderservice.domain.model;

import static lombok.EqualsAndHashCode.Include;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {

    public Member(String memberId) {
        this.memberId = memberId;
    }

    @Include
    private String memberId;
    private String firstName;
    private String lastName;
    private String nickName;
}
