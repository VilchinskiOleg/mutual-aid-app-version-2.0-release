package org.tms.profile_service_core.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Profile;
import org.tms.profile_service_core.domain.service.client.AuthAdditionalClientService;
import org.tms.profile_service_core.domain.service.kafka.producer.ProfileTaskForRetryingProducer;
import org.tms.profile_service_core.domain.service.processor.IdGeneratorService;
import org.tms.profile_service_core.persistent.repository.ProfileRepository;

import java.net.ConnectException;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static org.tms.profile_service_core.utils.Constant.Errors.*;

@Component
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final boolean isRetryableTaskEnabled;
    private final ProfileRepository profileRepository;
    private final IdGeneratorService idGeneratorService;
    private final AuthAdditionalClientService authAdditionalClientService;
    private final ProfileTaskForRetryingProducer profileTaskForRetryingProducer;
    private final Mapper mapper;

    public ProfileServiceImpl(@Value("${enable-task-retry}") boolean isRetryableTaskEnabled,
                              ProfileRepository profileRepository,
                              IdGeneratorService idGeneratorService,
                              AuthAdditionalClientService authAdditionalClientService,
                              ProfileTaskForRetryingProducer profileTaskForRetryingProducer,
                              Mapper mapper) {
        this.isRetryableTaskEnabled = isRetryableTaskEnabled;
        this.profileRepository = profileRepository;
        this.idGeneratorService = idGeneratorService;
        this.authAdditionalClientService = authAdditionalClientService;
        this.profileTaskForRetryingProducer = profileTaskForRetryingProducer;
        this.mapper = mapper;
    }

    @Override
    public Profile create(Profile profile) {
        profile.setCreateAt(now());
        profile.setProfileId(idGeneratorService.generate());
        Profile savedProfile = null;
        boolean isAuthProfileCreatedSuccessfully = false;
        try {
            var dataProfile = mapper.map(profile, org.tms.profile_service_core.persistent.entity.Profile.class);
            savedProfile = mapper.map(profileRepository.save(dataProfile), Profile.class);
            isAuthProfileCreatedSuccessfully = authAdditionalClientService.createAuth(profile);
            notifyUser(savedProfile.getEmail());
            return savedProfile;
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConnectException && isRetryableTaskEnabled) {
                log.error("Failed connection to the Auth-Rest. That req will be sent as retryable task to appropriate topic.",
                        ex);
                roleBackCreatingProfile(savedProfile);
                profileTaskForRetryingProducer.sendTaskEvent(ex, profile, "CREATE_PROFILE");
                throw new ConflictException(FAIL_CREATING_NEW_PROFILE_RETRYABLE);
            }
            log.error("Unexpected error during creation auth profile", ex);
            if (nonNull(savedProfile) && !isAuthProfileCreatedSuccessfully) {
                roleBackCreatingProfile(savedProfile);
            }
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

    private void notifyUser(String email) {
        if (nonNull(email)) {
            //TODO:.. have to think, if I need notification in Profile and Auth simultaneously?
        }
    }
}