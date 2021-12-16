package org.tms.task_executor_service.config.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class ThreadSaveMethodDetails {

    private Integer lockTimeOut;
    private Method method;
}