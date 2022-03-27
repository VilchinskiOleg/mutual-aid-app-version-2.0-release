package org.tms.task_executor_service.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.task_executor_service.rest.model.payload.Payload;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Task {

    private String id;

    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Meta meta;
    private Payload payload;
}