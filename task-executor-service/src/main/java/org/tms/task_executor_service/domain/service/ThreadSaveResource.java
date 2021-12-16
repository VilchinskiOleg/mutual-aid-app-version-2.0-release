package org.tms.task_executor_service.domain.service;

import java.util.concurrent.locks.Lock;

public interface ThreadSaveResource {

    Lock getLock();
}