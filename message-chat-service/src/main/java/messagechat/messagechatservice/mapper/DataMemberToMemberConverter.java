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
        destination.setProfileId(source.getProfileId());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}