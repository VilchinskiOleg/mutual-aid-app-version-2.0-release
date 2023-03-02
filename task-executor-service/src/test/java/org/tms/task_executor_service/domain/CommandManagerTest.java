package org.tms.task_executor_service.domain;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.spy;
import static org.tms.task_executor_service.domain.model.Task.Type.CREATE_PROFILE;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.task_executor_service.config.client.ProfileRestClient;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.model.payload.CreateProfilePayload;
import org.tms.task_executor_service.domain.service.CommandManager;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import org.tms.task_executor_service.domain.service.client.TaskExecutionProvider;
import org.tms.task_executor_service.domain.service.command.Command;

@TestInstance(PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class CommandManagerTest {

    @InjectMocks
    private CommandManager commandManager;
    @Spy
    private final List<TaskExecutionProvider> executionProviders = spy(ArrayList.class);
    @InjectMocks
    private final TaskExecutionProvider profileTaskExecutionProvider = spy(ProfileClientService.class);
    @Mock
    private Mapper mapper;
    @Mock //TODO: also can use @Spy for injection ProfileRestClient to TaskExecutionProvider (not used it in this test, only for example *)
    private ProfileRestClient profileRestClient;

    @BeforeAll
    void initAll() {
        executionProviders.add(profileTaskExecutionProvider);
    }

    @Test
    void retrieve_command_if_found_execution_provider_for_it() {
        Task createProfileTask = getCreateProfileTask();
        Command command = commandManager.retrieveCommand(createProfileTask);
        assertEquals(CREATE_PROFILE.getCommandImplClass().getSimpleName(), command.getName());
    }

    private Task getCreateProfileTask() {
        Task task = new Task();
        task.setInternalId("internalId");
        task.setType(CREATE_PROFILE);
        task.setCreatedAt(now());
        CreateProfilePayload payload = new CreateProfilePayload();
        task.setPayload(payload);
        return task;
    }
}