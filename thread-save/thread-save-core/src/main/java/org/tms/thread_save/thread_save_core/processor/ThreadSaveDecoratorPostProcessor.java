package org.tms.thread_save.thread_save_core.processor;

import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Arrays.stream;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.*;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;

import org.tms.thread_save.thread_save_api.annotation.ThreadSaveMethod;
import org.tms.thread_save.thread_save_api.marker.ThreadSaveResource;
import org.tms.thread_save.thread_save_core.config.ThreadSaveProperties;
import org.tms.thread_save.thread_save_core.model.MethodDetails;
import org.tms.thread_save.thread_save_core.processor.handler.ThreadSaveInvocationHandler;

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

        if (asList(beanClass.getInterfaces()).contains(ThreadSaveResource.class)) {
            Integer lockTimeOutByProps = properties.getLockTimeOut();

            Stream<Method> interfaceMethods = stream(beanClass.getInterfaces()).flatMap(intF -> stream(intF.getMethods()));
            List<Method> classMethods = asList(beanClass.getMethods());

            Map<Method, Method> classMethodByInterfaceMethod = interfaceMethods.collect(toMap(
                                          im -> im,
                                          im -> classMethods.stream()
                                                            .filter(cm -> {
                                                                var imParams = asList(im.getParameterTypes());
                                                                var secondParams = asList(cm.getParameterTypes());
                                                                return StringUtils.equals(im.getName(), cm.getName()) &&
                                                                        isEqualCollection(imParams, secondParams);
                                                            })
                                                            .findFirst()
                                                            .orElseThrow(() -> new BeanCreationException("Cannot create proxy bean, because cannot math class and interface methods for it."))
            ));
            List<MethodDetails> threadSaveMethods = classMethodByInterfaceMethod.entrySet().stream()
                                                                                           .filter(entry -> entry.getValue().isAnnotationPresent(ThreadSaveMethod.class))
                                                                                           .map(entry -> {
                                                                                               ThreadSaveMethod annotation = entry.getValue().getAnnotation(ThreadSaveMethod.class);
                                                                                               Integer lockTimeOut = annotation.lockTimeOut() > INTEGER_ZERO ? annotation.lockTimeOut() :
                                                                                                    nonNull(lockTimeOutByProps) ? lockTimeOutByProps : DEFAULT_LOCK_TIME_OUT;
                                                                                               return new MethodDetails(entry.getKey(), lockTimeOut);
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

        Lock lock;
        try {
            lock = (Lock) beanClass.getMethod(GET_LOCK).invoke(bean);
        } catch (Exception ex) {
            log.error("Unexpected error while retrieve Lock for thread-save resource.", ex);
            throw new BeanCreationException("Cannot create proxy bean");
        }

        if (nonNull(lock)) {
            return newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    new ThreadSaveInvocationHandler(threadSaveMethods, lock, bean));
        }

        return bean;
    }
}