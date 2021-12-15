package messagechat.messagechatservice.rest.message.response;

import lombok.Getter;
import lombok.Setter;
import messagechat.messagechatservice.rest.model.Dialog;
import java.util.Set;

@Getter
@Setter
public class DialogsPageResponse extends PageResponse {

    private Set<Dialog> dialogs;
}