package org.tms.task_executor_service.domain.service.command;

import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import ort.tms.mutual_aid.profile_service.client.model.Profile;

@Slf4j
public class CreateProfileCommand extends BaseCommand<CreateProfilePayload> {

    private final ProfileClientService clientApi;
    private final Mapper mapper;

    public CreateProfileCommand(CreateProfilePayload payload,
                                ProfileClientService clientApi,
                                Mapper mapper) {
        super(payload);
        this.clientApi = clientApi;
        this.mapper = mapper;
    }

    @Override
    public boolean apply() {
        var payload = super.getPayload();
        var profile = mapper.map(payload, Profile.class);
        try {
            Profile createdProfile = clientApi.createProfile(profile);
            log.info("Successful created profile={}", createdProfile);
            return true;
        } catch (ConflictException ex) {
            log.error("Unexpected error while execute {} with payload={}", getCommandName(), payload, ex);
            return false;
        }
    }
}