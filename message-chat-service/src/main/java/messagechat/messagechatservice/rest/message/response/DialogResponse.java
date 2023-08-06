package messagechat.messagechatservice.rest.message.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import messagechat.messagechatservice.rest.model.Dialog;
import org.exception.handling.autoconfiguration.model.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
public class DialogResponse extends BaseResponse {

    private Dialog dialog;

    public DialogResponse(Dialog dialog) {
        this.dialog = dialog;
    }
}