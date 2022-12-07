package org.tms.task_executor_service.persistent.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.tms.task_executor_service.utils.Constant.Mongo.INTERNAL_ID;

import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.tms.task_executor_service.persistent.entity.Task;

public class TaskExtendedRepositoryImpl implements TaskExtendedRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public long removeTasks(List<String> ids) {
        Query query = new Query(where(INTERNAL_ID).in(ids));
        return mongoTemplate.remove(Task.class).matching(query).all().getDeletedCount();
    }

    @Override
    public List<Task> findAllByInternalIds(Set<String> ids) {
        Query query = new Query(where(INTERNAL_ID).in(ids));
        return mongoTemplate.find(query, Task.class);
    }
}