package messagechat.messagechatservice.mapper;

import static org.springframework.util.CollectionUtils.isEmpty;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import java.util.HashSet;

@Component
public class DialogToDataDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.persistent.entity.Dialog> {

    @Override
    protected messagechat.messagechatservice.persistent.entity.Dialog getDestination() {
        return new messagechat.messagechatservice.persistent.entity.Dialog();
    }

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.persistent.entity.Dialog destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getInternalId());
        if (!isEmpty(source.getMembers())) {
            destination.setMembers(mapper.map(source.getMembers(), new HashSet<>(), Member.class));
        }
        destination.setStatus(mapper.map(source.getStatus()));
        destination.setType(mapper.map(source.getType()));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setCreateByMemberId(source.getCreateByMemberId());
        destination.setModifyByMemberId(source.getModifyByMemberId());
    }
}