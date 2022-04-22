package org.tms.task_executor_service.persistent.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.tms.task_executor_service.persistent.entity.payload.Payload;

@Getter
@Setter
@Document(collection = "task-executor-service.task")
public class Task {

    @Id
    private String id;
    private String internalId;

    private String type;
    @CreatedDate
    private LocalDateTime createdAt;

    private Meta meta;
    @DBRef
    private Payload payload;
}