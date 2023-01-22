package org.tms.profile_service_core.domain.service;

import org.tms.profile_service_core.domain.model.Profile;

public interface ProfileService {

    Profile create(Profile profile);

    Profile findByProfileIdRequired(String profileId);
}