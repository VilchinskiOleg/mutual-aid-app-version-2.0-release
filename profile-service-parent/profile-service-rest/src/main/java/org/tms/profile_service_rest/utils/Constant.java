package org.tms.profile_service_rest.utils;

import java.time.LocalDateTime;

public class Constant {

    public static class Service {

        public static final Long GENERATE_ID_U_64_TICKS_MASK = 0x3FFFFFFFFFFFFFFFL;
        public static final Long GENERATE_ID_U_64_LOCAL_MASK = 0x8000000000000000L;
        public static final LocalDateTime GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
    }

    public static class Errors {

        public static final String PROFILE_CONTACTS_RULE = "validation.profile.contacts.rule";
        public static final String PROFILE_GENDER_RULE = "validation.profile.gender.rule";
        public static final String PROFILE_BIRTHDAY_RULE = "validation.profile.birthday.rule";
        public static final String PROFILE_CONTACT_TYPE_RULE = "validation.profile.contact.type.rule";
        public static final String PROFILE_GENDER_REQUIRED = "validation.profile.gender.required";
        public static final String PROFILE_NAME_LOCALE_RULE = "validation.profile.name.locale.rule";
        public static final String PROFILE_NAMES_RULE = "validation.profile.names.rule";

        public static final String PROFILE_NOT_FUND = "profile.not.fund";
    }
}
