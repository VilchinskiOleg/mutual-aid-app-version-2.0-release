package org.tms.task_executor_service.persistent.prcessor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.entity.payload.Payload;

import javax.annotation.Resource;

import static java.util.Objects.nonNull;

@Component
public class TaskCascadeSaveMongoEventListener extends AbstractMongoEventListener<Task> {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Task> event) {
        Payload payload = event.getSource().getPayload();
        if (nonNull(payload)) {
            Payload savedPayload = mongoTemplate.save(payload);
            event.getSource().setPayload(savedPayload);
        }
        super.onBeforeConvert(event);
    }
}