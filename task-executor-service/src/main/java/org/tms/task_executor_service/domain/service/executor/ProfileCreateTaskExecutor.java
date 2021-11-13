package org.tms.task_executor_service.domain.service.executor;

import org.exception.handling.autoconfiguration.model.Error;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;

@Component
public class ProfileCreateTaskExecutor extends ProfileTaskExecutor {

    @Override
    public void execute(CreateProfilePayload payload) {

    }

    @Override
    public boolean support(CreateProfilePayload payload, Error error) {
        return false;
    }
}