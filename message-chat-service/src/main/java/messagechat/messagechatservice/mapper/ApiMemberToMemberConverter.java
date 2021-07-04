package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.rest.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class ApiMemberToMemberConverter extends BaseConverter<Member, messagechat.messagechatservice.domain.model.Member> {

    @Override
    protected messagechat.messagechatservice.domain.model.Member getDestination() {
        return new messagechat.messagechatservice.domain.model.Member();
    }

    @Override
    public void convert(Member source, messagechat.messagechatservice.domain.model.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
