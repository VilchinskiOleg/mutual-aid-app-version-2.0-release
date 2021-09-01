package org.tms.profile_service_rest.persistent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tms.profile_service_rest.persistent.entity.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {

}
