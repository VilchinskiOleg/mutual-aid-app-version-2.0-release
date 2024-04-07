package org.tms.task_executor_service.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.command.AbstractCommand;
import org.tms.task_executor_service.domain.service.command.DefaultGenericAbstractCommandImpl;

import java.lang.reflect.Method;

import static java.util.Objects.isNull;
import static org.tms.task_executor_service.utils.Constant.Errors.CANNOT_RETRIEVE_COMMAND;

/**
 * Handles each incoming {@link Task} and convert it to appropriate {@link AbstractCommand} implementation
 * according to the {@link Task.Type#getCommandImplClass()}
 */
@Slf4j
@Component
public class InitializationCommandManager {

    public AbstractCommand<?> retrieveCommand(Task task) {
        Class<?> commandImplClass = task.getType().getCommandImplClass();
        Method commandImplMethod = null;
        if (isNull(commandImplClass)) {
            throw new RuntimeException("Error: CommandImplClass cannot be null");
        } else if (!AbstractCommand.class.isAssignableFrom(commandImplClass)) {
            try {
                Class<?> commandImplMethodMarker = task.getType().getCommandImplMethodMarker();
                if (isNull(commandImplMethodMarker)) {
                    throw new RuntimeException("Er.");
                }
                var constructor = DefaultGenericAbstractCommandImpl.class.getDeclaredConstructor(Task.class, Object.class, Method.class);
                var command = constructor.newInstance(task, commandImplClass.getDeclaredConstructor().newInstance(), null);
                //TODO: have to think about cast to necessary Generic Type!
                return command;
            } catch (Exception ex) {
                log.error("Er. - ???");
                throw new ConflictException(CANNOT_RETRIEVE_COMMAND);
            }
        } else {
            try {
                return (AbstractCommand<?>) commandImplClass.getDeclaredConstructor(Task.class).newInstance(task);
            } catch (Exception ex) {
                log.error("Unexpected error while creating new command instance for implementation ={} by task ={}", task, ex);
                throw new ConflictException(CANNOT_RETRIEVE_COMMAND);
            }
        }
    }
}