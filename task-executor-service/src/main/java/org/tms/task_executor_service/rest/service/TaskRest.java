package org.tms.task_executor_service.rest.service;

import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiOperation;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import org.tms.task_executor_service.domain.service.TaskExecutionService;
import org.tms.task_executor_service.rest.model.Task;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskRest {

    @Resource
    private TaskExecutionService taskExecutionService;
    @Resource
    private Mapper mapper;

    @Api
    @ApiOperation(value = "${task.operation.get-tasks}")
    @GetMapping(path = "/{tasksAmount}")
    @ResponseStatus(OK)
    List<Task> getTasks(@PathVariable Integer tasksAmount) {
        var tasks = taskExecutionService.getTasks(tasksAmount);
        return mapper.map(tasks, new ArrayList<>(), Task.class);
    }

    @Api
    @ApiOperation(value = "${task.operation.execute-tasks-by-ids}")
    @GetMapping(path = "/execute/ids/{tasksIds}")
    @ResponseStatus(OK)
    List<Task> executeTasksByIds(@MatrixVariable Set<String> tasksIds) {
        var tasks = taskExecutionService.executeTasks(tasksIds);
        return mapper.map(tasks, new ArrayList<>(), Task.class);
    }

    @Api
    @ApiOperation(value = "${task.operation.execute-tasks-by-amount}")
    @GetMapping(path = "/execute/amount/{tasksAmount}")
    @ResponseStatus(OK)
    List<Task> executeTasksByAmount(@PathVariable Integer tasksAmount) {
        var tasks = taskExecutionService.executeTasks(tasksAmount);
        return mapper.map(tasks, new ArrayList<>(), Task.class);
    }
}