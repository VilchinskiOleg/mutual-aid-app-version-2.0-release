package org.tms.task_executor_service.domain;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.tms.task_executor_service.utils.MapperUtils.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.tms.task_executor_service.domain.service.CommandExecutor;
import org.tms.task_executor_service.domain.service.CommandManager;
import org.tms.task_executor_service.domain.service.TaskExecutionServiceImp;
import org.tms.task_executor_service.domain.service.client.ProfileClientService;
import org.tms.task_executor_service.domain.service.command.Command;
import org.tms.task_executor_service.domain.service.command.CreateProfileCommand;
import org.tms.task_executor_service.mapper.DataTaskToTaskConverter;
import org.tms.task_executor_service.mapper.TaskToDataTaskConverter;
import org.tms.task_executor_service.mapper.service.PayloadMappingManager;
import org.tms.task_executor_service.persistent.entity.Error;
import org.tms.task_executor_service.persistent.entity.Meta;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.entity.payload.CreateProfilePayload;
import org.tms.task_executor_service.persistent.entity.payload.profile.Contact;
import org.tms.task_executor_service.persistent.entity.payload.profile.Name;
import org.tms.task_executor_service.persistent.repository.TaskRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Disabled //TODO: That test doesn't work properly. Check it and fix the mistake!
@ExtendWith(MockitoExtension.class)
@TestInstance(PER_CLASS)
public class TaskExecutionServiceTest {

    public static final Integer TASKS_AMOUNT = 3;

    @InjectMocks
    private TaskExecutionServiceImp taskExecutionService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CommandExecutor commandExecutor;
    @Mock
    private CommandManager commandManager;
    @Spy
    private ProfileClientService profileClientService;


    /**
     * Inject @payloadMappingManager to the task converter in order to use it for retrieving destination class for payload:
     */
    @Spy
    private PayloadMappingManager payloadMappingManager = new PayloadMappingManager();
    @InjectMocks
    private DataTaskToTaskConverter dataTaskToTaskConverter = new DataTaskToTaskConverter();
    @InjectMocks
    private TaskToDataTaskConverter taskToDataTaskConverter = new TaskToDataTaskConverter();


    @Spy
    private Mapper mapper = getMapper();
    @Spy
    private final ExecutorService asyncTaskExecutor = Executors.newFixedThreadPool(TASKS_AMOUNT);

    @BeforeAll
    void initAll() {
        initDefaultTaskMapper(mapper, payloadMappingManager, dataTaskToTaskConverter, taskToDataTaskConverter);
    }

    @Test
    void execute_tasks_by_amount() {
        when(taskRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(getTasks()));
        when(taskRepository.removeTasks((List<String>) any())).thenReturn(0L);

        when(commandManager.retrieveCommand((org.tms.task_executor_service.domain.model.Task) any()))
                .thenAnswer(args -> new CreateProfileCommand(args.getArgument(0), profileClientService, mapper));

        taskExecutionService.executeTasks(TASKS_AMOUNT);
        verify(commandExecutor, times(TASKS_AMOUNT)).execute((Command) any());
    }

    private List<Task> getTasks() {
        return IntStream.range(INTEGER_ZERO, TASKS_AMOUNT)
                        .boxed()
                        .map(index -> getTask())
                        .collect(toList());
    }

    private Task getTask() {
        var task = new Task();
        task.setType("CREATE_PROFILE");
        task.setPayload(new CreateProfilePayload("male", now(), List.of(new Name()), List.of(new Contact())));
        task.setMeta(getMeta());
        return task;
    }

    private Meta getMeta() {
        var meta = new Meta();
        meta.setFlowId("flow_id");
        meta.setErrorDetails(new Error());
        return meta;
    }
}