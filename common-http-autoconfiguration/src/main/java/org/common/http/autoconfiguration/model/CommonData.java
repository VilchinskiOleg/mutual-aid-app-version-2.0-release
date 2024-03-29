package org.common.http.autoconfiguration.model;

import lombok.Getter;
import lombok.Setter;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class CommonData {

    private Locale locale;
    private String flowId;

    private HttpSession httpSession;
    private Map<String, String> headers;
}