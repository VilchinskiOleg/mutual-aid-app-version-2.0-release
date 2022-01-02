package org.tms.thread_save.thread_save_core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.lang.reflect.Method;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MethodDetails {

    private Integer lockTimeOut;
    private Method method;
}