package messagechat.messagechatservice.domain.service;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import messagechat.messagechatservice.persistent.repository.MemberRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Profile;

import static java.util.Optional.ofNullable;
import static messagechat.messagechatservice.mapper.DataMemberToMemberForJoinDialogConverter.ADD_MEMBER_TO_DIALOG;
import static messagechat.messagechatservice.util.Constant.Errors.MEMBER_NOT_FUND;

@Component
@RequiredArgsConstructor
public class MemberServiceImpl {

    private final ProfileClientService profileClientService;
    private final MemberRepository memberRepository;
    private final Mapper mapper;

    public Member getMemberByIdRequired(String profileId) {
        return memberRepository.findByProfileId(profileId)
                .map(dataMember -> mapper.map(dataMember, Member.class, ADD_MEMBER_TO_DIALOG))
                .orElseGet(() -> fetchMemberFromProfileService(profileId));

    }

    private Member fetchMemberFromProfileService(String profileId) {
        Profile profile = ofNullable(profileClientService.getProfileById(profileId))
                .orElseThrow(() -> new ConflictException(MEMBER_NOT_FUND));
        return mapper.map(profile, Member.class);
    }
}