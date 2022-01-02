package org.tms.thread_save.thread_save_core.processor;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import org.tms.thread_save.thread_save_api.annotation.ThreadSaveMethod;
import org.tms.thread_save.thread_save_api.marker.ThreadSaveResource;
import org.tms.thread_save.thread_save_core.config.ThreadSaveProperties;
import org.tms.thread_save.thread_save_core.model.MethodDetails;

@Slf4j
@Component
public class ThreadSaveDecoratorPostProcessor implements BeanPostProcessor {

    public static final String GET_LOCK = "getLock";
    public static final Integer DEFAULT_LOCK_TIME_OUT = 1;

    private final Map<String, List<MethodDetails>> threadSaveMethodsByBeanName = new HashMap<>();
    @Resource
    private ThreadSaveProperties properties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (beanClass.isInstance(ThreadSaveResource.class)) {
            Integer lockTimeOutByProps = properties.getLockTimeOut();
            List<MethodDetails> threadSaveMethods = Arrays.stream(beanClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(ThreadSaveMethod.class))
                    .map(method -> {
                        ThreadSaveMethod annotation = method.getAnnotation(ThreadSaveMethod.class);
                        Integer lockTimeOut = annotation.lockTimeOut() > INTEGER_ZERO ? annotation.lockTimeOut() :
                                nonNull(lockTimeOutByProps) ? lockTimeOutByProps : DEFAULT_LOCK_TIME_OUT;
                        return new MethodDetails(lockTimeOut, method);
                    })
                    .collect(toList());
            threadSaveMethodsByBeanName.put(beanName, threadSaveMethods);
        }

        return bean;
    }

    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        List<MethodDetails> threadSaveMethods = threadSaveMethodsByBeanName.get(beanName);
        if (isEmpty(threadSaveMethods)) {
            return bean;
        }
        Class<?> beanClass = bean.getClass();
        Lock lock = (Lock) beanClass.getMethod(GET_LOCK).invoke(bean);

        if (nonNull(lock)) {
            return newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    MethodDetails currentMethodDetails = threadSaveMethods.stream()
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