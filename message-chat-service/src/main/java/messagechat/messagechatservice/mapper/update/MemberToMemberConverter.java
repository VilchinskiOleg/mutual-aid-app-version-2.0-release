package messagechat.messagechatservice.mapper.update;

import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MemberToMemberConverter extends BaseConverter<Member, Member> {

    @Override
    protected Member getDestination() {
        throw new UnsupportedOperationException("Such converter can be used with existed destinations only, not new");
    }

    @Override
    public void convert(Member source, Member destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setNickName(source.getNickName());
    }
}