package messagechat.messagechatservice.rest.message.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateDialogRequest {

    private String dialogName;
    private Set<String> memberIds;
}