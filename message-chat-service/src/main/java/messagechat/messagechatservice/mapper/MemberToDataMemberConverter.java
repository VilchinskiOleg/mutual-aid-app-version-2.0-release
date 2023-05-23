package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.entity.MemberInfo;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToDataMemberConverter extends BaseConverter<Member, messagechat.messagechatservice.persistent.entity.Member> {

    @Override
    public void convert(Member source, messagechat.messagechatservice.persistent.entity.Member destination) {
        destination.setId(source.getId());
        destination.setProfileId(source.getProfileId());
        destination.setMemberInfo(MemberInfo.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .nickName(source.getNickName())
                .build());
    }
}