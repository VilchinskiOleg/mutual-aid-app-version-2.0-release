package org.tms.task_executor_service.persistent.entity.payload.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {

    private String locale;
    private String firstName;
    private String lastName;
}