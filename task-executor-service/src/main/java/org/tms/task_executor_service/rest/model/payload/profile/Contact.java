package org.tms.task_executor_service.rest.model.payload.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact {

    private String type;
    private String value;
}