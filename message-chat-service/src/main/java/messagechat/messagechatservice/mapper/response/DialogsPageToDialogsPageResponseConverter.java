package messagechat.messagechatservice.mapper.response;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.rest.message.response.DialogsPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
public class DialogsPageToDialogsPageResponseConverter extends PageToPageResponseConverter<Dialog, DialogsPageResponse> {

    @Override
    protected DialogsPageResponse getDestination() {
        return new DialogsPageResponse();
    }

    @Override
    public void convert(Page<Dialog> source, DialogsPageResponse destination) {
        destination.setDialogs(mapper.map(source.getContent(),
                                          new HashSet<>(),
                                          messagechat.messagechatservice.rest.model.Dialog.class));
        super.convert(source, destination);
    }
}