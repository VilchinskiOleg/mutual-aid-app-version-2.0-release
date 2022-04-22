package org.tms.task_executor_service.domain.model.payload;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.task_executor_service.domain.model.payload.profile.Contact;
import org.tms.task_executor_service.domain.model.payload.profile.Name;

@Getter
@Setter
@ToString
public class CreateProfilePayload extends Payload {

    private String gender;
    private LocalDate birthday;
    private List<Name> names;
    private List<Contact> contacts;
}