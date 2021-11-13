package org.tms.task_executor_service.persistent.entity.payload;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProfilePayload extends Payload {

    private String name;
    private String lastName;
    private LocalDate birthday;
}