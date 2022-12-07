package org.tms.task_executor_service.mapper.payload;

import org.tms.task_executor_service.mapper.service.PayloadMappingManager;
import javax.annotation.Resource;

public interface PayloadMappingRegistrationVisitor {

    @Resource
    default void registerPayload(PayloadMappingManager payloadMappingManager) {
        payloadMappingManager.registerDestinationType(this.getSourceType(), this.getDestinationType());
    }

    Class<?> getSourceType();

    Class<?> getDestinationType();
}