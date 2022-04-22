package org.tms.task_executor_service.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.exception.handling.autoconfiguration.model.Error;

@Getter
@Setter
@ToString
public class Meta {

    private String flowId;
    private String client;
    private Error errorDetails;
    private String requestDetails;
}