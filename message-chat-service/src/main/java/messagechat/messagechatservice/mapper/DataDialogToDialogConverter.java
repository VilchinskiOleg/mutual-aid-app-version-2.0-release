package messagechat.messagechatservice.mapper;

import static org.springframework.util.CollectionUtils.isEmpty;
import static messagechat.messagechatservice.domain.model.Dialog.Status;

import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.entity.Dialog;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DataDialogToDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.domain.model.Dialog> {

    @Override
    protected messagechat.messagechatservice.domain.model.Dialog getDestination() {
        return new messagechat.messagechatservice.domain.model.Dialog();
    }

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.domain.model.Dialog destination) {
        destination.setId(source.getId());
        destination.setDialogId(source.getInternalId());
        if (!isEmpty(source.getMembers())) {
            destination.setMembers(mapper.map(source.getMembers(), new ArrayList<>(), Member.class));
        }
        destination.setStatus(mapper.map(source.getStatus(), Status.class));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}
