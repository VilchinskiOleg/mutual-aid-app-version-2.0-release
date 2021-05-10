package messagechat.messagechatservice.domain.mapper;

import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToApiMemberConverter extends BaseConverter<Member, messagechat.messagechatservice.rest.model.Member> {

    @Override
    protected messagechat.messagechatservice.rest.model.Member getDestination() {
        return new messagechat.messagechatservice.rest.model.Member();
    }

    @Override
    public void convert(Member source, messagechat.messagechatservice.rest.model.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
