package order.orderservice.persistent.mongo.entity;

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
