package org.tms.task_executor_service.domain.service.executor;

import org.exception.handling.autoconfiguration.model.Error;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.payload.ProfilePayload;

@Component
public class ProfileCreateTaskExecutor extends ProfileTaskExecutor {

    @Override
    public void execute(ProfilePayload payload) {

    }

    @Override
    public boolean support(ProfilePayload payload, Error error) {
        return false;
    }
}