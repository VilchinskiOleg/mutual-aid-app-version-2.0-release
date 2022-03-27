package org.tms.task_executor_service.rest.model.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.task_executor_service.rest.model.payload.profile.Contact;
import org.tms.task_executor_service.rest.model.payload.profile.Name;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateProfilePayload extends Payload {

    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private List<Name> names;
    private List<Contact> contacts;
}