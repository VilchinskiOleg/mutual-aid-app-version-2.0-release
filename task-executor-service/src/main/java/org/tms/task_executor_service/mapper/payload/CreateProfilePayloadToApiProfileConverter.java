package org.tms.task_executor_service.mapper.payload;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;
import ort.tms.mutual_aid.profile_service.client.model.Profile;

@Component
public class CreateProfilePayloadToApiProfileConverter extends BaseConverter<CreateProfilePayload, Profile> {

    @Override
    protected Profile getDestination() {
        return new Profile();
    }

    @Override
    public void convert(CreateProfilePayload source, Profile destination) {
        destination.setBirthday(null);
        destination.setContacts(null);
        destination.setCreateAt(null);
        destination.setGender(null);
        destination.setId(null);
        destination.setModifyAt(null);
        destination.setNames(null);
        destination.setProfileId(null);
    }
}