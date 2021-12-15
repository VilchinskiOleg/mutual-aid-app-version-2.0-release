package messagechat.messagechatservice.rest.message.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateMessageRequest extends PageRequest {

    @NotBlank
    private String authorId;
    private String dialogId;
    @NotBlank
    @ApiModelProperty(dataType = "string", value = "Hello World!", example = "Hello World!")
    private String description;
}