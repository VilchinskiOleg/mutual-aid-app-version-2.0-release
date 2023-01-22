package org.tms.profile_service_core.utils;

import java.time.LocalDateTime;

public class Constant {

    public static class Service {

        public static final Long GENERATE_ID_U_64_TICKS_MASK = 0x3FFFFFFFFFFFFFFFL;
        public static final Long GENERATE_ID_U_64_LOCAL_MASK = 0x8000000000000000L;
        public static final LocalDateTime GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
    }

    public static class Errors {

        public static final String PROFILE_NOT_FUND = "profile.not.fund";
        public static final String FAIL_CREATING_AUTH_PROFILE = "fail.creating.auth.profile";
        public static final String FAIL_CREATING_NEW_PROFILE = "fail.creating.new.profile";
    }
}