package org.tms.task_executor_service.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.domain.dto.Event;
import org.tms.task_executor_service.domain.service.listener.AbstractCommandsGroupListener;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
public class CommandsGroupListenerAsyncManager implements EventPublisher {

    private final Map<Class<?>, ? extends AbstractCommandsGroupListener<?>> listenersByClazz;

    public CommandsGroupListenerAsyncManager(List<? extends AbstractCommandsGroupListener<?>> listeners) {
        this.listenersByClazz = listeners.stream()
                .collect(toMap(Object::getClass, listener -> listener));
    }

    @Override
    //todo: have problem with @Async (only for dev testing). Investigate and fix!
//    @Async
    public void sendEvent(Event event) {
//        try {
//            Thread.sleep(15000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        var currentListener = ofNullable(listenersByClazz.get(event.getListenerClazz()));
        currentListener.ifPresent(listener -> {
            listener.runCommands(event.getCommands());
        });
    }

    public boolean isContainAppropriateListener(Class<?> listenerImplClazz) {
        return listenersByClazz.containsKey(listenerImplClazz);
    }
}