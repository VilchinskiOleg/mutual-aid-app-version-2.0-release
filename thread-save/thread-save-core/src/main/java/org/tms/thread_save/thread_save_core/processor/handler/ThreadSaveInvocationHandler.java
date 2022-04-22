package org.tms.thread_save.thread_save_core.processor.handler;

import static java.lang.String.format;

import lombok.extern.slf4j.Slf4j;
import org.tms.thread_save.thread_save_core.model.MethodDetails;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
public class ThreadSaveInvocationHandler implements InvocationHandler {

    private final List<MethodDetails> threadSaveMethods;
    private final Lock lock;
    private final Object object;

    public ThreadSaveInvocationHandler(List<MethodDetails> threadSaveMethods, Lock lock, Object wrapedObject) {
        this.threadSaveMethods = threadSaveMethods;
        this.lock = lock;
        this.object = wrapedObject;
    }

    @Override
    public Object invoke(Object proxyObject, Method method, Object[] args) throws Throwable {
        MethodDetails methodDetails = threadSaveMethods.stream()
                                                       .filter(md -> md.getMethod().equals(method))
                                                       .findFirst()
                                                       .orElseThrow(() -> {
                                                           log.error("Unexpected error while invoke thread-save method = {}. Cannot get method details from list = {}",
                                                                   method, threadSaveMethods);
                                                           return new IllegalStateException(format("Cannot get method details for thread-save method = %s", method));
                                                       });

        return invokeThreadSaveLogic(this.object, method, args, methodDetails.getLockTimeOut(), lock);
    }

    private Object invokeThreadSaveLogic(Object object,
                                          Method method,
                                          Object[] args,
                                          Integer lockTimeOut,
                                          Lock lock) throws Throwable {
        try {
            if (lock.tryLock(lockTimeOut, TimeUnit.SECONDS)) {
                try {
                    return method.invoke(object, args);
                } finally {
                    lock.unlock();
                }
            }
            log.warn("Resource = {} is not allowed", object.getClass().getSimpleName());
            return null;
        } catch (InterruptedException ex) {
            log.error("Unexpected error while getting access to resource: {}", object.getClass().getSimpleName(), ex);
            throw ex;
        }
    }
}