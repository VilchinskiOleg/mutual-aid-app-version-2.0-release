package org.tms.task_executor_service.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.exception.handling.autoconfiguration.model.Error;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.tms.task_executor_service.domain.model.payload.Payload;

@Getter
@Setter
public class Task {

    private String id;

    private String internalId;
    private String flowId;
    private LocalDateTime createdAt;
    private String client;

    private Payload payload;
    private Error errorDetails;
    private String requestDetails;

    private Type type;

    @AllArgsConstructor
    public enum Type {

        CREATE_PROFILE(1);

        private final int priority;
    }
}