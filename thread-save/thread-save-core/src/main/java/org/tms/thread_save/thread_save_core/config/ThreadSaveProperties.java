package org.tms.thread_save.thread_save_core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "thread-save-props")
public class ThreadSaveProperties {

    private Integer lockTimeOut; //sek

    public Integer getLockTimeOut() {
        return lockTimeOut;
    }
}