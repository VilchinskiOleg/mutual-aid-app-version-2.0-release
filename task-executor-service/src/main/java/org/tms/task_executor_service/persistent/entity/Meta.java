package org.tms.task_executor_service.persistent.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    private String flowId;
    private String client;
    private Error errorDetails;
    private String requestDetails;
}