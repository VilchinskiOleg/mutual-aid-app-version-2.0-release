package messagechat.messagechatservice.mapper;

import static org.springframework.util.CollectionUtils.isEmpty;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.rest.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DialogToApiDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.rest.model.Dialog> {

    @Override
    protected messagechat.messagechatservice.rest.model.Dialog getDestination() {
        return new messagechat.messagechatservice.rest.model.Dialog();
    }

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.rest.model.Dialog destination) {
        destination.setDialogId(source.getDialogId());
        if (!isEmpty(source.getMembers())) {
            destination.setMembers(mapper.map(source.getMembers(), new ArrayList<>(), Member.class));
        }
        destination.setStatus(mapper.map(source.getStatus()));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}
