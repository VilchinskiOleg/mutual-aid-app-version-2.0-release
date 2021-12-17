package org.tms.task_executor_service.domain.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.tms.task_executor_service.domain.model.payload.Payload;

@AllArgsConstructor
public abstract class BaseCommand<T extends Payload> implements Command {

    @Getter
    private final T payload;

    protected String getCommandName() {
        return this.getClass().getSimpleName();
    }
}