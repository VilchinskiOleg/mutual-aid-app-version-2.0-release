package org.tms.thread_save.thread_save_api.marker;

import java.util.concurrent.locks.Lock;

public interface ThreadSaveResource {

    Lock getLock();
}