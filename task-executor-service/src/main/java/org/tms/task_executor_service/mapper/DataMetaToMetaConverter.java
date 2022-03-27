package org.tms.task_executor_service.mapper;

import org.exception.handling.autoconfiguration.model.Error;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.persistent.entity.Meta;

@Component
public class DataMetaToMetaConverter extends BaseConverter<Meta, org.tms.task_executor_service.domain.model.Meta> {

    @Override
    public void convert(Meta source, org.tms.task_executor_service.domain.model.Meta destination) {
        destination.setFlowId(source.getFlowId());
        destination.setClient(source.getClient());
        destination.setErrorDetails(mapper.map(source.getErrorDetails(), Error.class));
        destination.setRequestDetails(source.getRequestDetails());
    }
}