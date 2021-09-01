package org.tms.profile_service_rest.domain.service;

import org.springframework.stereotype.Component;
import org.tms.profile_service_rest.domain.service.processor.IdGeneratorService;
import org.tms.profile_service_rest.persistent.repository.ProfileRepository;
import javax.annotation.Resource;

@Component
public class ProfileServiceImpl implements ProfileService {

    @Resource
    private ProfileRepository profileRepository;
    @Resource
    private IdGeneratorService idGeneratorService;


}
