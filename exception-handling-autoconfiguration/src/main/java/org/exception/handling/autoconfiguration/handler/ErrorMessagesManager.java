package org.exception.handling.autoconfiguration.handler;

import static java.util.Objects.isNull;

import org.exception.handling.autoconfiguration.model.LocalizedErrorMessages;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class ErrorMessagesManager {

    @Resource
    private Map<String, LocalizedErrorMessages> localizedErrorMessagesConfig;

    @Nullable
    public String getLocalizedErrorMessage(String code, String lang) {
        LocalizedErrorMessages errorMessages = localizedErrorMessagesConfig.get(code);
        if (isNull(errorMessages)) {
            return null;
        }
        return errorMessages.getMessages()
                            .get(lang);
    }
}
