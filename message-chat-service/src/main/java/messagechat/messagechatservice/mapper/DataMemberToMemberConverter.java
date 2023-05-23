package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataMemberToMemberConverter extends BaseConverter<Member, messagechat.messagechatservice.domain.model.Member> {

    @Override
    protected messagechat.messagechatservice.domain.model.Member getDestination() {
        return new messagechat.messagechatservice.domain.model.Member();
    }

    @Override
    public void convert(Member source, messagechat.messagechatservice.domain.model.Member destination) {
        destination.setId(source.getId());
        destination.setProfileId(source.getProfileId());
        var memberInfo = source.getMemberInfo();
        destination.setFirstName(memberInfo.getFirstName());
        destination.setLastName(memberInfo.getLastName());
        destination.setNickName(memberInfo.getNickName());
    }
}