package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.mapper.service.PayloadMappingManager;
import org.tms.task_executor_service.rest.model.Meta;
import org.tms.task_executor_service.domain.model.Task;
import org.tms.task_executor_service.domain.model.payload.Payload;
import javax.annotation.Resource;

@Component
public class TaskToApiTaskConverter extends BaseConverter<Task, org.tms.task_executor_service.rest.model.Task> {

    @Resource
    private PayloadMappingManager payloadMappingManager;

    @Override
    public void convert(Task source, org.tms.task_executor_service.rest.model.Task destination) {
        destination.setId(source.getInternalId());
        destination.setType(mapper.map(source.getType()));
        destination.setCreatedAt(source.getCreatedAt());
        destination.setMeta(mapper.map(source.getMeta(), Meta.class));

        Payload payload = source.getPayload();
        destination.setPayload(mapper.map(payload, getPayloadDestinationType(payload)));
    }

    private Class<? extends org.tms.task_executor_service.rest.model.payload.Payload> getPayloadDestinationType(Payload payload) {
        return payloadMappingManager.getDestinationType(payload.getClass(), org.tms.task_executor_service.rest.model.payload.Payload.class);
    }
}