package org.tms.task_executor_service.persistent.repository;

import java.util.List;
import java.util.Set;
import org.tms.task_executor_service.persistent.entity.Task;

public interface TaskExtendedRepository {

    long removeTasks(List<String> ids);

    List<Task> findAllByInternalIds(Set<String> ids);
}