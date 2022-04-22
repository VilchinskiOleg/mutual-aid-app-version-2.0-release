package org.tms.thread_save.thread_save_api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.lang.Nullable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThreadSaveMethod {

    /**
     * Timeout unit is SECONDS.
     * @return
     */
    int lockTimeOut() default 0;
}