package org.tms.task_executor_service.mapper.payload;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;
import org.tms.task_executor_service.rest.model.payload.profile.Contact;
import org.tms.task_executor_service.rest.model.payload.profile.Name;
import java.util.ArrayList;

@Component
public class CreateProfilePayloadToApiCreateProfilePayloadConverter
        extends BaseConverter<CreateProfilePayload, org.tms.task_executor_service.rest.model.payload.CreateProfilePayload>
        implements PayloadMappingRegistrationVisitor {

    @Override
    public void convert(CreateProfilePayload source, org.tms.task_executor_service.rest.model.payload.CreateProfilePayload destination) {
        destination.setGender(source.getGender());
        destination.setBirthday(source.getBirthday());
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
    }
}