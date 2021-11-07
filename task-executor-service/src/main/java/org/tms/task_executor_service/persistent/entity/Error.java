package org.tms.task_executor_service.persistent.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

    private String code;
    private String message;
}