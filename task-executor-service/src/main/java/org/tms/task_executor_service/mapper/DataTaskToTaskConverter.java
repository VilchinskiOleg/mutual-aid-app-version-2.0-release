package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Meta;
import org.tms.task_executor_service.domain.model.Task.Type;
import org.tms.task_executor_service.domain.model.payload.Payload;
import org.tms.task_executor_service.persistent.entity.Task;

@Component
public class DataTaskToTaskConverter extends BaseConverter<Task, org.tms.task_executor_service.domain.model.Task> {

    @Override
    protected org.tms.task_executor_service.domain.model.Task getDestination() {
        return new org.tms.task_executor_service.domain.model.Task();
    }

    @Override
    public void convert(Task source, org.tms.task_executor_service.domain.model.Task destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getInternalId());
        destination.setType(mapper.map(source.getType(), Type.class));
        destination.setCreatedAt(source.getCreatedAt());
        destination.setMeta(mapper.map(source.getMeta(), Meta.class));
        destination.setPayload(mapper.map(source.getPayload(), Payload.class));
    }
}