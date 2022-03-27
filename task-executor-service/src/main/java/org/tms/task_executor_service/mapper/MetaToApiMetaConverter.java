package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.model.Meta;

@Component
public class MetaToApiMetaConverter extends BaseConverter<Meta, org.tms.task_executor_service.rest.model.Meta> {

    @Override
    public void convert(Meta source, org.tms.task_executor_service.rest.model.Meta destination) {
        destination.setFlowId(source.getFlowId());
        destination.setClient(source.getClient());
        destination.setErrorDetails(source.getErrorDetails());
        destination.setRequestDetails(source.getRequestDetails());
    }
}