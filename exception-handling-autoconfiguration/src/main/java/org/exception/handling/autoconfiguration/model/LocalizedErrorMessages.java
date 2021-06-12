package org.exception.handling.autoconfiguration.model;

import static java.util.Objects.isNull;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LocalizedErrorMessages {

    private String code;
    private Map<String, String> messages;

    public Map<String, String> getMessages() {
        if (isNull(messages)) {
            messages = new HashMap<>();
        }
        return messages;
    }
}
