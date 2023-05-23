package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.entity.Dialog;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

import static messagechat.messagechatservice.domain.model.Dialog.Status;
import static messagechat.messagechatservice.domain.model.Dialog.Type;

@Component
public class DataDialogToDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.domain.model.Dialog> {

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.domain.model.Dialog destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getDialogId());
        destination.setVersion(source.getVersion());

        destination.setMembers(mapper.map(source.getMembers(), new HashSet<>(), Member.class));

        destination.setStatus(mapper.map(source.getStatus(), Status.class));
        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setCreateByMemberId(source.getCreateByMemberId());
        destination.setModifyByMemberId(source.getModifyByMemberId());
    }
}