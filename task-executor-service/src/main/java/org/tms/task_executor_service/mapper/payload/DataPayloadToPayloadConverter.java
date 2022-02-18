package org.tms.task_executor_service.mapper.payload;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.persistent.entity.payload.Payload;

@Component
public class DataPayloadToPayloadConverter extends BaseConverter<Payload, org.tms.task_executor_service.domain.model.payload.Payload> {

    @Override
    protected org.tms.task_executor_service.domain.model.payload.Payload getDestination() {
        return null;
    }

    @Override
    public void convert(Payload source, org.tms.task_executor_service.domain.model.payload.Payload destination) {
        this.getClass().get
        //todo: write!
    }
}