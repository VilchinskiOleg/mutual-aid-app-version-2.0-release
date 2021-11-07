package org.tms.task_executor_service.domain.service.executor;

import javax.annotation.Resource;
import org.tms.task_executor_service.domain.model.payload.ProfilePayload;
import ort.tms.mutual_aid.profile_service.client.model.Profile;

public abstract class ProfileTaskExecutor implements TaskExecutor<ProfilePayload> {

    @Resource
    protected Object profileClientService;

}