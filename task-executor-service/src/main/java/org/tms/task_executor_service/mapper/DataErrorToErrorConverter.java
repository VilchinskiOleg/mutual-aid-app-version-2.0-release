package org.tms.task_executor_service.mapper;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.persistent.entity.Error;

@Component
public class DataErrorToErrorConverter extends BaseConverter<Error, org.exception.handling.autoconfiguration.model.Error> {

    @Override
    public void convert(Error source, org.exception.handling.autoconfiguration.model.Error destination) {
        destination.setCode(source.getCode());
        destination.setMessage(source.getMessage());
    }
}