package messagechat.messagechatservice.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    @ApiModelProperty(dataType = "string", example = "1-2-3")
    private String memberId;
    @ApiModelProperty(dataType = "string", example = "Artur")
    private String firstName;
    @ApiModelProperty(dataType = "string", example = "Bishap")
    private String lastName;
    @ApiModelProperty(dataType = "string", example = "Mechanic")
    private String nickName;
}
