package org.tms.task_executor_service.persistent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    private String flowId;
    private String client;
    private Error errorDetails;
    private String requestDetails;
}