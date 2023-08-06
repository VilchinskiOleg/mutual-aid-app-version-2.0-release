package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static messagechat.messagechatservice.mapper.MemberToDataMemberForLinkToMessageConverter.LINK_AUTHOR_TO_MESSAGE;

@Component
@ModelMapperContext(value = LINK_AUTHOR_TO_MESSAGE)
public class MemberToDataMemberForLinkToMessageConverter extends BaseConverter<Member, messagechat.messagechatservice.persistent.entity.Member> {

    public static final String LINK_AUTHOR_TO_MESSAGE = "link_author_to_message";

    @Override
    public void convert(Member source, messagechat.messagechatservice.persistent.entity.Member destination) {
        destination.setId(source.getId());
        destination.setProfileId(source.getProfileId());
    }
}