package org.tms.thread_save.thread_save_core.processor.handler;

import static java.util.Objects.nonNull;

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

    /**
     * If it will be thread-save method we will find it in method details and will run thread-save logic; if not -> it's
     * common (not thread-save) method and we should just invoke method with out extra logic.
     */
    @Override
    public Object invoke(Object proxyObject, Method method, Object[] args) throws Throwable {
        MethodDetails methodDetails = threadSaveMethods.stream()
                                                       .filter(md -> md.getMethod().equals(method))
                                                       .findFirst()
                                                       .orElse(null);
        if (nonNull(methodDetails)) {
            return invokeThreadSaveLogic(this.object, method, args, methodDetails.getLockTimeOut(), lock);
        }
        return method.invoke(this.object, args);
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