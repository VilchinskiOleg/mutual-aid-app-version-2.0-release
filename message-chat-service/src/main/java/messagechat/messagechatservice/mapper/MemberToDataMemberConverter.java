package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToDataMemberConverter extends BaseConverter<Member, messagechat.messagechatservice.persistent.entity.Member> {

    @Override
    protected messagechat.messagechatservice.persistent.entity.Member getDestination() {
        return new messagechat.messagechatservice.persistent.entity.Member();
    }

    @Override
    public void convert(Member source, messagechat.messagechatservice.persistent.entity.Member destination) {
        destination.setMemberId(source.getMemberId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}
