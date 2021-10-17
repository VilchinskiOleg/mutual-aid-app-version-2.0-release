package messagechat.messagechatservice.util;

import static java.util.regex.Pattern.compile;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Constant {

    public static class Service {

        public static final Long GENERATE_ID_U_64_TICKS_MASK = 0x3FFFFFFFFFFFFFFFL;
        public static final Long GENERATE_ID_U_64_LOCAL_MASK = 0x8000000000000000L;
        public static final LocalDateTime GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN = LocalDateTime.of(1582, 10, 15, 0, 0, 0);

        public static class GoogleTranslateApi {

            public static final String HOST_HEADER = "X-RapidAPI-Host";
            public static final String TOKEN_HEADER = "X-RapidAPI-Key";
            public static final String ENCODING_HEADER = "Accept-Encoding";

            public static final String TEXT_DELIMITER = "%20";
            public static final String SOURCE_LANG_FORM_BODY_KEY = "&source=";
            public static final String TARGET_LANG_FORM_BODY_KEY = "&target=";
            public static final String TEXT_FORM_BODY_KEY = "q=";

            public static final Pattern TEXT_DELIMITING_PATTERN = compile(" +");
        }

        public static class Mongo {

            public static final String MEMBERS = "members";
            public static final String PROFILE_ID = "profileId";
        }
    }

    public static class Errors {

        public static final String MESSAGE_NOT_FOUND = "message.not.found";
        public static final String TRANSLATION_ERROR = "translation.error";
    }
}