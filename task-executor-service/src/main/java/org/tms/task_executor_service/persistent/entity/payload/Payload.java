package org.tms.task_executor_service.persistent.entity.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "task-executor-service.payload")
public abstract class Payload {

    @Id
    private String id;
}