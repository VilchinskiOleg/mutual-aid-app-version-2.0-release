package org.tms.task_executor_service.domain.service;

import static org.tms.task_executor_service.utils.Constant.Errors.CANNOT_RETRIEVE_COMMAND;
import static org.tms.task_executor_service.utils.Constant.Errors.TASK_EXECUTION_PROVIDER_NOT_FOUND;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.client.TaskExecutionProvider;
import org.tms.task_executor_service.domain.service.command.Command;

@Slf4j
@Component
public class CommandManager {

    @Resource
    private List<TaskExecutionProvider> executionProviders;
    @Resource
    private Mapper mapper;

    public Command retrieveCommand(Task task) {
        Class<?> commandImplClass = task.getType().getCommandImplClass();
        TaskExecutionProvider<?> currentProvider = executionProviders.stream()
                                                                     .filter(provider -> provider.getSupportedTasks().contains(task.getType()))
                                                                     .findFirst()
                                                                     .orElseThrow(() -> new ConflictException(TASK_EXECUTION_PROVIDER_NOT_FOUND));
        try {
            return (Command) commandImplClass.getConstructors()[0]
                                             .newInstance(task, currentProvider.getProvider(), mapper);
        } catch (Exception ex) {
            log.error("Unexpected error while creating new command instance by task={}", task, ex);
            throw new ConflictException(CANNOT_RETRIEVE_COMMAND);
        }
    }
}