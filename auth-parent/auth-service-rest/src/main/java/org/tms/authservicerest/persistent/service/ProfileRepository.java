package org.tms.authservicerest.persistent.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tms.authservicerest.persistent.model.Profile;

public interface ProfileRepository extends ExtendedProfileRepository, MongoRepository<Profile, String> {
}