package org.tms.task_executor_service.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapper.autoconfiguration.ModelMapperConfig;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.mutual_aid.profile_service.client.model.Profile;
import org.tms.task_executor_service.config.thread.RunListenersAsyncExecutorConfig;
import org.tms.task_executor_service.domain.service.CommandsGroupListenerAsyncManager;
import org.tms.task_executor_service.domain.service.EventPublisher;
import org.tms.task_executor_service.domain.service.InitializationCommandManager;
import org.tms.task_executor_service.domain.service.TaskExecutionServiceImp;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import org.tms.task_executor_service.domain.service.listener.ProfileCommandsGroupListener;
import org.tms.task_executor_service.mapper.DataErrorToErrorConverter;
import org.tms.task_executor_service.mapper.DataMetaToMetaConverter;
import org.tms.task_executor_service.mapper.DataTaskToTaskConverter;
import org.tms.task_executor_service.mapper.payload.DataCreateProfilePayloadToCreateProfilePayloadConverterVisitor;
import org.tms.task_executor_service.mapper.payload.api_model.CreateProfilePayloadToApiProfileConverter;
import org.tms.task_executor_service.mapper.service.PayloadMappingManager;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(PER_CLASS)
@ContextConfiguration(classes = {
        // model mapping (Command):
        ModelMapperConfig.class,
        DataTaskToTaskConverter.class,
        DataMetaToMetaConverter.class,
        DataErrorToErrorConverter.class,
        DataCreateProfilePayloadToCreateProfilePayloadConverterVisitor.class,
        PayloadMappingManager.class,
        // model mapping (Client Api):
        CreateProfilePayloadToApiProfileConverter.class,
        // services:
        TaskExecutionServiceImp.class,
        InitializationCommandManager.class,
        CommandsGroupListenerAsyncManager.class,
        RunListenersAsyncExecutorConfig.class,
        // listeners:
        ProfileCommandsGroupListener.class
})
public class TaskExecutionServiceTest {

    private static final int TASKS_AMOUNT = 2;

    @Resource
    private TaskExecutionServiceImp taskExecutionService;
    @Resource
    private InitializationCommandManager initializationCommandManager;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private Mapper mapper;

    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private ProfileClientService profileClientService;


    /**
     * Check executing some amount of tasks by Scheduling Job:
     */
    @Test
    @SneakyThrows({FileNotFoundException.class, IOException.class})
    void execute_tasks_by_amount() {
        @Cleanup var inStream = getClass().getClassLoader().getResourceAsStream("tasks.json");
        var objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("org.tms.task_executor_service.persistent.entity.payload")
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new JavaTimeModule());
        List<Task> tasks = objectMapper.readValue(inStream, new TypeReference<List<Task>>() {});

        when(taskRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(tasks));
        when(profileClientService.createProfile((Profile) any())).thenAnswer(args -> args.getArgument(0));

        taskExecutionService.executeTasks(TASKS_AMOUNT);
        verify(profileClientService, times(TASKS_AMOUNT)).createProfile((Profile) any());
    }
}