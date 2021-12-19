package org.tms.task_executor_service.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.exception.handling.autoconfiguration.model.Error;

@Getter
@Setter
public class Meta {

    private String flowId;
    private String client;
    private Error errorDetails;
    private String requestDetails;
}