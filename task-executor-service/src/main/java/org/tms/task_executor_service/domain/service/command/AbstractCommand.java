package org.tms.task_executor_service.domain.service.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.client.CommandExecutionProvider;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public abstract class AbstractCommand<T extends CommandExecutionProvider>
        implements Comparable<AbstractCommand<T>> {

    protected final Task task;

    /**
     * Main method.
     */
    public abstract void apply(T taskExecutorProvider, Mapper mapper);

    @Override
    public int compareTo(@NotNull AbstractCommand<T> obj) {
        return this.task.compareTo(obj.getTask());
    }
}