package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Meta;
import org.tms.task_executor_service.persistent.entity.Error;

@Component
public class MetaToDataMetaConverter extends BaseConverter<Meta, org.tms.task_executor_service.persistent.entity.Meta> {

    @Override
    protected org.tms.task_executor_service.persistent.entity.Meta getDestination() {
        return new org.tms.task_executor_service.persistent.entity.Meta();
    }

    @Override
    public void convert(Meta source, org.tms.task_executor_service.persistent.entity.Meta destination) {
        destination.setFlowId(source.getFlowId());
        destination.setClient(source.getClient());
        destination.setErrorDetails(mapper.map(source.getErrorDetails(), Error.class));
        destination.setRequestDetails(source.getRequestDetails());
    }
}