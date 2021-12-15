package messagechat.messagechatservice.rest.message.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateMessageRequest extends PageRequest {

    @NotBlank
    private String messageId;
    @NotBlank
    @ApiModelProperty(dataType = "string", value = "Hello World!", example = "Hello World!")
    private String description;
}