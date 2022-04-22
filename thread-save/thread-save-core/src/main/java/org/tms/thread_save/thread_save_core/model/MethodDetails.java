package org.tms.thread_save.thread_save_core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.lang.reflect.Method;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MethodDetails {

    private Method method;

    private Integer lockTimeOut;
}