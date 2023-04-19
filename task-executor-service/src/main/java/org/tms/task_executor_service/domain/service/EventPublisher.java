package org.tms.task_executor_service.domain.service;

import org.tms.task_executor_service.domain.dto.Event;

public interface EventPublisher {

    void sendEvent(Event event);
}