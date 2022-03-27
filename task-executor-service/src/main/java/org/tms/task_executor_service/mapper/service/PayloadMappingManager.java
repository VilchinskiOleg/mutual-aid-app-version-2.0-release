package org.tms.task_executor_service.mapper.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;

import static org.tms.task_executor_service.utils.Constant.Errors.SOURCE_PAYLOAD_KEY_NOT_FOUND;

@Component
public class PayloadMappingManager {

    private final Map<ConverterKey, Class<?>> destinationClassByConverterKey = new HashMap<>();

    public void registerDestinationType(Class<?> sourceClass, Class<?> destinationClass) {
        destinationClassByConverterKey.put(buildKey(sourceClass, destinationClass.getSuperclass()), destinationClass);
    }

    public <E> Class<? extends E> getDestinationType(Class<?> sourceClass, Class<E> destinationParentClass) {
        Class<?> destinationType = Optional.ofNullable(destinationClassByConverterKey.get(buildKey(sourceClass, destinationParentClass)))
                                           .orElseThrow(() -> new ConflictException(SOURCE_PAYLOAD_KEY_NOT_FOUND));
        return (Class<? extends E>) destinationType;
    }

    private ConverterKey buildKey(Class<?> sourceClass, Class<?> destinationParentClass) {
        return new ConverterKey(sourceClass, destinationParentClass);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    private class ConverterKey {

        private Class<?> sourceClass;
        private Class<?> destinationParentClass;
    }
}