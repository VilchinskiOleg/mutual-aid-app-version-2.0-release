package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static messagechat.messagechatservice.mapper.DataMemberToMemberForJoinDialogConverter.ADD_MEMBER_TO_DIALOG;

@Component
@ModelMapperContext(value = ADD_MEMBER_TO_DIALOG)
public class DataMemberToMemberForJoinDialogConverter extends BaseConverter<Member, messagechat.messagechatservice.domain.model.Member> {

    public static final String ADD_MEMBER_TO_DIALOG = "add_member_to_dialog";

    @Override
    public void convert(Member source, messagechat.messagechatservice.domain.model.Member destination) {
        destination.setId(source.getId());
        destination.setProfileId(source.getProfileId());
    }
}