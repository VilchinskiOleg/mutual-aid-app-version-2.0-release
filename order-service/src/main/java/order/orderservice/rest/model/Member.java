package order.orderservice.rest.model;

import static order.orderservice.util.Constant.Errors.ORDER_OWNER_REQUIRED;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    @NotBlank(message = ORDER_OWNER_REQUIRED)
    private String memberId;
    private String firstName;
    private String lastName;
    private String nickName;
}
