package org.tms.task_executor_service.domain.service.listener;

import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;

@Component
public class ProfileCommandsGroupListener extends AbstractCommandsGroupListener<ProfileClientService> {

    public ProfileCommandsGroupListener(ProfileClientService commandExecutionProvider, Mapper mapper) {
        super(commandExecutionProvider, mapper);
    }
}