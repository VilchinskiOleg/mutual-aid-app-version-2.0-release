package org.tms.task_executor_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.tms.task_executor_service.domain.service.listener.AbstractCommandsGroupListener;

import java.util.SortedSet;

@Getter
@Setter
@AllArgsConstructor
public class Event {

    private Class<? extends AbstractCommandsGroupListener<?>> listenerClazz;
    private SortedSet<Object> commands;
}