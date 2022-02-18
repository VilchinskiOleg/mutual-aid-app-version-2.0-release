package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.persistent.entity.Meta;
import org.tms.task_executor_service.persistent.entity.payload.Payload;

@Component
public class TaskToDataTaskConverter extends BaseConverter<Task, org.tms.task_executor_service.persistent.entity.Task> {

    @Override
    protected org.tms.task_executor_service.persistent.entity.Task getDestination() {
        return new org.tms.task_executor_service.persistent.entity.Task();
    }

    @Override
    public void convert(Task source, org.tms.task_executor_service.persistent.entity.Task destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getInternalId());
        destination.setType(mapper.map(source.getType()));
        destination.setCreatedAt(source.getCreatedAt());
        destination.setMeta(mapper.map(source.getMeta(), Meta.class));
        destination.setPayload(mapper.map(source.getPayload(), Payload.class));
    }
}