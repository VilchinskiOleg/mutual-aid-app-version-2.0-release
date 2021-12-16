package org.tms.task_executor_service.domain.service.processor;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.config.TaskExecutionProperties;
import org.tms.task_executor_service.config.meta.ThreadSaveMethodDetails;
import org.tms.task_executor_service.domain.service.ThreadSaveResource;
import org.tms.task_executor_service.domain.service.annotation.ThreadSaveMethod;
import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
public class ThreadSaveDecoratorPostProcessor implements BeanPostProcessor {

    public static final String GET_LOCK = "getLock";
    public static final Integer DEFAULT_LOCK_TIME_OUT = 1;

    private final Map<String, List<ThreadSaveMethodDetails>> threadSaveMethodsByBeanName = new HashMap<>();
    @Resource
    private TaskExecutionProperties properties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (beanClass.isInstance(ThreadSaveResource.class)) {
            Integer lockTimeOutByProps = properties.getLockTimeOut();
            List<ThreadSaveMethodDetails> threadSaveMethods = Arrays.stream(beanClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(ThreadSaveMethod.class))
                    .map(method -> {
                        ThreadSaveMethod annotation = method.getAnnotation(ThreadSaveMethod.class);
                        Integer lockTimeOut = annotation.lockTimeOut() > INTEGER_ZERO ? annotation.lockTimeOut() :
                                nonNull(lockTimeOutByProps) ? lockTimeOutByProps : DEFAULT_LOCK_TIME_OUT;
                        return new ThreadSaveMethodDetails(lockTimeOut, method);
                    })
                    .collect(toList());
            threadSaveMethodsByBeanName.put(beanName, threadSaveMethods);
        }

        return bean;
    }

    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        List<ThreadSaveMethodDetails> threadSaveMethods = threadSaveMethodsByBeanName.get(beanName);
        Class<?> beanClass = bean.getClass();
        Lock lock = (Lock) beanClass.getMethod(GET_LOCK).invoke(bean);

        if (!threadSaveMethods.isEmpty() && nonNull(lock)) {
            return newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    ThreadSaveMethodDetails currentMethodDetails = threadSaveMethods.stream()
                            .filter(methodDetails -> methodDetails.getMethod().equals(method))
                            .findFirst()
                            .orElse(null);
                    if (nonNull(currentMethodDetails)) {
                        return invokeThreadSaveLogics(proxy, method, args, currentMethodDetails.getLockTimeOut(), lock);
                    }
                    return method.invoke(proxy, args);
                }
            });
        }

        return bean;
    }

    private Object invokeThreadSaveLogics(Object proxy, Method method, Object[] args, Integer lockTimeOut, Lock lock) throws Throwable {
        try {
            if (lock.tryLock(lockTimeOut, TimeUnit.SECONDS)) {
                try {
                    return method.invoke(proxy, args);
                } finally {
                    lock.unlock();
                }
            }
            log.warn("Resource: {} is not allowed", proxy.getClass().getSimpleName());
            return null;
        } catch (InterruptedException ex) {
            log.error("Unexpected error while getting access to resource: {}", proxy.getClass().getSimpleName(), ex);
            throw ex;
        }
    }
}