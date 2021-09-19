package org.tms.profile_service_rest.domain.service;

import org.tms.profile_service_rest.domain.model.Profile;

public interface ProfileService {

    Profile create(Profile profile);

    Profile findByProfileIdRequired(String profileId);
}
