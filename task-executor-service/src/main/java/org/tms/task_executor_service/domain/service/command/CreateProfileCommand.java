package org.tms.task_executor_service.domain.service.command;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import ort.tms.mutual_aid.profile_service.client.model.Profile;

@Slf4j
public class CreateProfileCommand extends BaseCommand {

    private final ProfileClientService clientApi;

    public CreateProfileCommand(Task task,
                                ProfileClientService clientApi,
                                Mapper mapper) {
        super(task, mapper);
        this.clientApi = clientApi;
    }

    @Override
    public boolean apply() {
        var payload = task.getPayload();
        var profile = mapper.map(payload, Profile.class);
        try {
            Profile createdProfile = clientApi.createProfile(profile);
            log.info("Successful created profile={}", createdProfile);
            return true;
        } catch (ConflictException ex) {
            log.warn("Fail creating profile by payload={}", payload, ex);
            return false;
        }
    }
}