package org.tms.task_executor_service.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.task_executor_service.domain.model.payload.Payload;
import org.tms.task_executor_service.domain.service.command.CreateProfileCommand;

@Getter
@Setter
@ToString
public class Task {

    private String id;
    private String internalId;

    private Type type;
    private LocalDateTime createdAt;

    private Meta meta;
    private Payload payload;

    @AllArgsConstructor
    @Getter
    public enum Type {

        CREATE_PROFILE(1, CreateProfileCommand.class);

        private final int priority;
        private final Class<?> commandImplClass;
    }
}