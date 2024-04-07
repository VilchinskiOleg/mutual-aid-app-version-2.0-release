package org.tms.task_executor_service.domain.service.listener;

import lombok.RequiredArgsConstructor;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.service.client.CommandExecutionProvider;
import org.tms.task_executor_service.domain.service.command.AbstractCommand;

import java.util.SortedSet;

/**
 * Class which is intended to serve some group of Commands{@link AbstractCommand} and run them by {@link CommandExecutionProvider} Implementation.
 * In order to achieve that this class just provide Command with CommandExecutionProvider which will be applied inside the Command.
 *
 * This class serve some group of Commands according to Command Payload and CommandExecutionProvider necessary to run them.
 *
 * @param <T> - Type of particular CommandExecutionProvider.
 */
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