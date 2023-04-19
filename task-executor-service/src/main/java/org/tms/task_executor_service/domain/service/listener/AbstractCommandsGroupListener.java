package org.tms.task_executor_service.domain.service.listener;

import lombok.RequiredArgsConstructor;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.service.client.CommandExecutionProvider;
import org.tms.task_executor_service.domain.service.command.AbstractCommand;

import java.util.SortedSet;

@RequiredArgsConstructor
public abstract class AbstractCommandsGroupListener<T extends CommandExecutionProvider>{

    private final T commandExecutionProvider;
    private final Mapper mapper;

    public void runCommands(SortedSet<Object> commands){
        for (Object command : commands) {
            var castedCommand = (AbstractCommand<T>) command;
            castedCommand.apply(commandExecutionProvider, mapper);
        }
    }
}