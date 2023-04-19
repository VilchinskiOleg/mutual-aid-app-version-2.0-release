package org.tms.task_executor_service.domain.service.command;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import org.tms.mutual_aid.profile_service.client.model.Profile;

@Slf4j
public class CreateProfileCommand extends AbstractCommand<ProfileClientService> {

    public CreateProfileCommand(Task task) {
        super(task);
    }

    @Override
    public void apply(ProfileClientService clientApi, Mapper mapper) {
        var payload = task.getPayload();
        var profile = mapper.map(payload, Profile.class);
        try {
            Profile createdProfile = clientApi.createProfile(profile);
            log.info("Successful created profile={}", createdProfile);
        } catch (ConflictException ex) {
            log.warn("Fail creating profile by payload={}", payload, ex);
        }
    }
}