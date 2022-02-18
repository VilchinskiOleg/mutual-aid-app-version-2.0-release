package org.tms.task_executor_service.domain.service.command;

import lombok.Getter;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;

public abstract class BaseCommand implements Command {

    @Getter
    protected final Task task;
    protected final Mapper mapper;
    @Getter
    protected boolean successful;

    public BaseCommand(Task task, Mapper mapper) {
        this.task = task;
        this.mapper = mapper;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void apply() {
        successful = doCommand();
    }

    abstract protected boolean doCommand();
}