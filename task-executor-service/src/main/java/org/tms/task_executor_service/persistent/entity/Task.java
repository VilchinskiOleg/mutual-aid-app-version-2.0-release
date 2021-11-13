package org.tms.task_executor_service.persistent.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.tms.task_executor_service.persistent.entity.payload.Payload;

@Getter
@Setter
@Document(collection = "task")
public class Task {

    @Id
    private String id;

    private String internalId;
    private String flowId;
    private LocalDateTime createdAt;
    private String client;

    private Payload payload;
    private Error errorDetails;
    private String requestDetails;

    private String type;
}