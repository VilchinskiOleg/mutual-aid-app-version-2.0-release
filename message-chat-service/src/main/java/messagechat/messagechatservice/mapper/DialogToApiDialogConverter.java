package messagechat.messagechatservice.mapper;

import static org.springframework.util.CollectionUtils.isEmpty;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DialogToApiDialogConverter extends BaseConverter<Dialog, messagechat.messagechatservice.rest.model.Dialog> {

    @Override
    protected messagechat.messagechatservice.rest.model.Dialog getDestination() {
        return new messagechat.messagechatservice.rest.model.Dialog();
    }

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.rest.model.Dialog destination) {
        destination.setDialogId(source.getInternalId());
        if (!isEmpty(source.getMembers())) {
            destination.setMemberIds(source.getMembers()
                    .stream()
                    .map(Member::getProfileId)
                    .collect(Collectors.toSet()));
        }
        destination.setStatus(mapper.map(source.getStatus()));
        destination.setType(mapper.map(source.getType()));
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setCreateByMemberId(source.getCreateByMemberId());
        destination.setModifyByMemberId(source.getModifyByMemberId());
    }
}