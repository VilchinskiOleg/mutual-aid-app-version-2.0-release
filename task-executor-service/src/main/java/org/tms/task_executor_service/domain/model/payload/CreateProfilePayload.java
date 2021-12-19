package org.tms.task_executor_service.domain.model.payload;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateProfilePayload implements Payload {

    private String name;
    private String lastName;
    private LocalDate birthday;
}