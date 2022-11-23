package org.tms.profile_service_rest.domain.service;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static org.tms.profile_service_rest.utils.Constant.Errors.FAIL_CREATING_NEW_PROFILE;
import static org.tms.profile_service_rest.utils.Constant.Errors.PROFILE_NOT_FUND;

import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.model.Profile;
import org.tms.profile_service_rest.domain.service.client.AuthAdditionalClientService;
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
    private AuthAdditionalClientService authAdditionalClientService;
    @Resource
    private Mapper mapper;

    @Override
    public Profile create(Profile profile) {
        profile.setCreateAt(now());
        profile.setProfileId(idGeneratorService.generate());
        Profile savedProfile = null;
        try {
            var dataProfile = mapper.map(profile, org.tms.profile_service_rest.persistent.entity.Profile.class);
            savedProfile = mapper.map(profileRepository.save(dataProfile), Profile.class);
            authAdditionalClientService.createAuth(profile);
            return savedProfile;
        } catch (Exception ex) {
            roleBackCreatingProfile(savedProfile);
            throw new ConflictException(FAIL_CREATING_NEW_PROFILE);
        }
    }

    @Override
    public Profile findByProfileIdRequired(String profileId) {
        var dataProfileWrapper = profileRepository.findByProfileId(profileId);
        return mapper.map(dataProfileWrapper.orElseThrow(() -> new ConflictException(PROFILE_NOT_FUND)), Profile.class);
    }

    private void roleBackCreatingProfile(Profile savedProfile) {
        if (nonNull(savedProfile)) {
            profileRepository.deleteById(savedProfile.getProfileId());
        }
    }
}