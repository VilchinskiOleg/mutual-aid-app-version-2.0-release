package org.tms.task_executor_service.persistent.repository;

import org.tms.task_executor_service.persistent.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}