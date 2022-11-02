package org.common.http.autoconfiguration.interceptor;

import static java.util.Objects.isNull;
import static org.common.http.autoconfiguration.utils.Constant.DEFAULT_LANG;
import static org.common.http.autoconfiguration.utils.Constant.LANG_HEADER;

import lombok.AllArgsConstructor;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@AllArgsConstructor
public class RequestCommonDataInterceptor implements HandlerInterceptor {

    private CommonData commonData;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        commonData.setLocale(new Locale(extractLang(request)));
//        setFlowIdToHider_If_itIsNull;
        commonData.setHttpSession(request.getSession());
        commonData.setHeaders(extractHeaders(request));
        return true;
    }

    private String extractLang(HttpServletRequest request) {
        Object lang = request.getHeader(LANG_HEADER);
        if (isNull(lang)) {
            return DEFAULT_LANG;
        }
        return lang.toString().toLowerCase();
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        var headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            headers.put(key, request.getHeader(key));
        }
        return headers;
    }
}
