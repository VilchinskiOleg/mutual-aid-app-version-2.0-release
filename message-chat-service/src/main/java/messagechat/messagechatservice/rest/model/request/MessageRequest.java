package messagechat.messagechatservice.rest.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import messagechat.messagechatservice.rest.model.Member;

@Getter
@Setter
public class MessageRequest {

    @ApiModelProperty(dataType = "string", value = "Hello World_1!", example = "Hello World_2!")
    private String description;
    private Member author;
}
