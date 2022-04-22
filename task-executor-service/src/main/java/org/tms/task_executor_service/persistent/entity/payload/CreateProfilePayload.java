package org.tms.task_executor_service.persistent.entity.payload;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tms.task_executor_service.persistent.entity.payload.profile.Contact;
import org.tms.task_executor_service.persistent.entity.payload.profile.Name;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfilePayload extends Payload {

    private String gender;
    private LocalDate birthday;
    private List<Name> names;
    private List<Contact> contacts;
}