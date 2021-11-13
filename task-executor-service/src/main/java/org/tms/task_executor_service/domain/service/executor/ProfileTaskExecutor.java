package org.tms.task_executor_service.domain.service.executor;

import javax.annotation.Resource;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;

public abstract class ProfileTaskExecutor implements TaskExecutor<CreateProfilePayload> {

    @Resource
    protected Object profileClientService;

}