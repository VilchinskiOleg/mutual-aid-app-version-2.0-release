package org.common.http.autoconfiguration.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

public class Constant {

    @UtilityClass
    public static class Service {

        public static final Long GENERATE_ID_U_64_TICKS_MASK = 0x3FFFFFFFFFFFFFFFL;
        public static final Long GENERATE_ID_U_64_LOCAL_MASK = 0x8000000000000000L;
        public static final LocalDateTime GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
    }

    public static final String LANG_HEADER = "x-lang";
    public static final String DEFAULT_LANG = "en";
    public static final String [] PROCESSED_URL_PATTERNS = {"/api/*", "/api/**"};

    public static final int OK_HTTP_CODE = 200;
}
