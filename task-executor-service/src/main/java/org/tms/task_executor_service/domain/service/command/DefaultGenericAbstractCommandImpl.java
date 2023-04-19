package org.tms.task_executor_service.domain.service.command;

import lombok.extern.slf4j.Slf4j;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.client.CommandExecutionProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class DefaultGenericAbstractCommandImpl<T extends CommandExecutionProvider> extends AbstractCommand<T> {

    private final Object commandImplObject;
    private final Method commandImplMethod;

    public DefaultGenericAbstractCommandImpl(Task task,
                                             Object commandImplObject,
                                             Method commandImplMethod) {
        super(task);
        this.commandImplObject = commandImplObject;
        this.commandImplMethod = commandImplMethod;
    }

    @Override
    public void apply(T taskExecutorProvider, Mapper mapper) {
        try {
            commandImplMethod.invoke(commandImplObject, task, taskExecutorProvider, mapper);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Unexpected error during invocation command = {}", this);
            throw new RuntimeException(e);
        }
    }
}