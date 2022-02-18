package org.tms.task_executor_service.domain;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.tms.task_executor_service.utils.MapperUtils.getMapper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.tms.task_executor_service.domain.service.CommandExecutor;
import org.tms.task_executor_service.domain.service.CommandManager;
import org.tms.task_executor_service.domain.service.TaskExecutionService;
import org.tms.task_executor_service.mapper.DataTaskToTaskConverter;
import org.tms.task_executor_service.mapper.payload.DataPayloadToPayloadConverter;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.entity.payload.CreateProfilePayload;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

@TestInstance(PER_CLASS)
public class TaskExecutionServiceTest {

    public static final Integer TASKS_AMOUNT = 3;

    @InjectMocks
    private TaskExecutionService taskExecutionService;
    @Spy
    private final Mapper mapper = getMapper(List.of(new DataTaskToTaskConverter(), new DataPayloadToPayloadConverter()));
    @InjectMocks
    private final ExecutorService asyncTaskExecutorService = Executors.newFixedThreadPool(10);
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CommandExecutor commandExecutor;
    @Mock
    private CommandManager commandManager;

    @BeforeAll
    void initAll() {
        initMocks(this);
    }

    @Test
    void some_test() {
        when(taskRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(getTasks()));
        when(taskRepository.removeTasks((List<String>) any())).thenReturn(0);
        taskExecutionService.executeTasks(TASKS_AMOUNT);
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
        task.setPayload(new CreateProfilePayload("name", "lastName", now()));
        return task;
    }
}