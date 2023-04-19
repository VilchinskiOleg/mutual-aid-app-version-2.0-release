package org.tms.task_executor_service.mapper.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.tms.common.kafka.avro.TaskEvent;
import org.tms.task_executor_service.persistent.entity.Error;
import org.tms.task_executor_service.persistent.entity.Meta;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.entity.payload.CreateProfilePayload;

public class TaskEventToDataTaskConverter extends BaseConverter<TaskEvent, Task> {

    @Override
    public void convert(TaskEvent source, Task destination) {
        var metaData = new Meta(
                (String) source.getFlowId(),
                null,
                new Error((String) source.getErrorCode(), (String) source.getErrorMessage()),
                null);
        destination.setType((String) source.getType());
        destination.setMeta(metaData);

        //TODO: improve (*) mapping for different payload impls:
        try {
            var payload = new ObjectMapper().readValue((String) source.getPayload(), CreateProfilePayload.class);
            destination.setPayload(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}