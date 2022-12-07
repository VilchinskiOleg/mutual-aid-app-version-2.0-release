package org.tms.task_executor_service.domain.model.payload.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Name {

    private String locale;
    private String firstName;
    private String lastName;
}