package org.tms.task_executor_service.domain.model;

import lombok.*;
import org.tms.task_executor_service.domain.model.payload.Payload;
import org.tms.task_executor_service.domain.service.command.CreateProfileCommand;
import org.tms.task_executor_service.domain.service.listener.AbstractCommandsGroupListener;
import org.tms.task_executor_service.domain.service.listener.ProfileCommandsGroupListener;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Setter
@ToString
public class Task implements Comparable<Task> {

    private String id;
    private String internalId;

    private Type type;
    private LocalDateTime createdAt;

    private Meta meta;
    private Payload payload;

    @Override
    public int compareTo(@NotNull Task obj) {
        return Comparator
                .comparingInt(task -> ((Task) task).getType().getPriority())
                .thenComparing(task -> ((Task) task).getId())
                .compare(this, obj);
    }

    @AllArgsConstructor
    @Getter
    public enum Type{

        CREATE_PROFILE(
                1,
                CreateProfileCommand.class,
                null,
                ProfileCommandsGroupListener.class);

        private final int priority;
        @NonNull
        private final Class<?> commandImplClass;
        private final Class<?> commandImplMethodMarker;
        @NonNull
        private final Class<? extends AbstractCommandsGroupListener<?>> commandsGroupListenerImplClass;
    }
}