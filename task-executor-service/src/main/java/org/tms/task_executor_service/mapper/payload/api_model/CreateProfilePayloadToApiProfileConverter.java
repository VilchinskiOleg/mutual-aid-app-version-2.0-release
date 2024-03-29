package org.tms.task_executor_service.mapper.payload.api_model;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;
import org.tms.mutual_aid.profile_service.client.model.Contact;
import org.tms.mutual_aid.profile_service.client.model.Name;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import java.util.ArrayList;

@Component
public class CreateProfilePayloadToApiProfileConverter extends BaseConverter<CreateProfilePayload, Profile> {

    @Override
    public void convert(CreateProfilePayload source, Profile destination) {
        destination.setGender(source.getGender());
        destination.setBirthday(source.getBirthday().toString());
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
    }
}