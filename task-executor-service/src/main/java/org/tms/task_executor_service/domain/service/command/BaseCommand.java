package org.tms.task_executor_service.domain.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;

@AllArgsConstructor
public abstract class BaseCommand implements Command {

    @Getter
    protected final Task task;
    protected final Mapper mapper;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}