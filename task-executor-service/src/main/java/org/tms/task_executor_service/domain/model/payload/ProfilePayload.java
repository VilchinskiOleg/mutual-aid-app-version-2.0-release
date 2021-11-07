package org.tms.task_executor_service.domain.model.payload;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePayload extends Payload {

    private String name;
    private String lastName;
    private LocalDate birthday;
}