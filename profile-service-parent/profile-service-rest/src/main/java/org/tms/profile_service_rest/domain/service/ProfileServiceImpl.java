package org.tms.profile_service_rest.domain.service;

import static java.time.LocalDateTime.now;
import static org.tms.profile_service_rest.utils.Constant.Errors.PROFILE_NOT_FUND;

import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Profile;
import org.tms.profile_service_rest.domain.service.processor.IdGeneratorService;
import org.tms.profile_service_rest.persistent.repository.ProfileRepository;
import javax.annotation.Resource;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Resource
    private ProfileRepository profileRepository;
    @Resource
    private IdGeneratorService idGeneratorService;
    @Resource
    private Mapper mapper;

    @Override
    public Profile create(Profile profile) {
        profile.setCreateAt(now());
        profile.setProfileId(idGeneratorService.generate());
        var dataProfile = mapper.map(profile, org.tms.profile_service_rest.persistent.entity.Profile.class);
        return mapper.map(profileRepository.save(dataProfile), Profile.class);
    }

    @Override
    public Profile findByProfileIdRequired(String profileId) {
        var dataProfileWrapper = profileRepository.findByProfileId(profileId);
        return mapper.map(dataProfileWrapper.orElseThrow(() -> new ConflictException(PROFILE_NOT_FUND)), Profile.class);
    }
}
