package org.tms.task_executor_service.domain.service.executor;

import org.exception.handling.autoconfiguration.model.Error;
import org.tms.task_executor_service.domain.model.payload.Payload;

public interface TaskExecutor<T extends Payload> {

    void execute(T payload);

    boolean support(T payload, Error error);
}