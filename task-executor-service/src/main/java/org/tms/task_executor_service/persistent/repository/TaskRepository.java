package org.tms.task_executor_service.persistent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tms.task_executor_service.persistent.entity.Task;

public interface TaskRepository extends MongoRepository<Task, String>, TaskExtendedRepository {
}