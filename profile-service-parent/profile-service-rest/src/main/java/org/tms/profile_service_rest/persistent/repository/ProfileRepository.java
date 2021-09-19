package org.tms.profile_service_rest.persistent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tms.profile_service_rest.persistent.entity.Profile;
import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, String> {

    Optional<Profile> findByProfileId(String profileId);
}
