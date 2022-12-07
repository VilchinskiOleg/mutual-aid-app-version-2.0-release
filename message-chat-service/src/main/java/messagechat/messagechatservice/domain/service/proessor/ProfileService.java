package messagechat.messagechatservice.domain.service.proessor;

import static java.util.Objects.isNull;
import static messagechat.messagechatservice.util.Constant.Errors.MEMBER_NOT_FUND;

import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import javax.annotation.Resource;

@Component
public class ProfileService {

    @Resource
    private ProfileClientService profileClientService;
    @Resource
    private Mapper mapper;

    public Member getMemberByIdRequired(String profileId) {
        Profile profile = profileClientService.getProfileById(profileId);
        if (isNull(profile)) {
            throw new ConflictException(MEMBER_NOT_FUND);
        }
        return mapper.map(profile, Member.class);
    }
}