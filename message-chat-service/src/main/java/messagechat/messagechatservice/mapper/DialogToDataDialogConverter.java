package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;


@Component
public class DialogToDataDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.persistent.entity.Dialog> {

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.persistent.entity.Dialog destination) {
        destination.setId(source.getId());
        destination.setDialogId(source.getInternalId());
        destination.setVersion(source.getVersion());
        // 'source.getMembers()' have lazy List initialization, so it will not provide as with NPE:
        source.getMembers().forEach(member -> destination.addMember(mapper.map(member, Member.class)));
        destination.setStatus(mapper.map(source.getStatus()));
        destination.setType(mapper.map(source.getType()));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setCreateByMemberId(source.getCreateByMemberId());
        destination.setModifyByMemberId(source.getModifyByMemberId());
    }
}