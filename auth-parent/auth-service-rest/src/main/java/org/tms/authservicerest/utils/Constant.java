package org.tms.authservicerest.utils;

import java.time.LocalDateTime;

public class Constant {

  public static class Service {

    public static final Long GENERATE_ID_U_64_TICKS_MASK = 0x3FFFFFFFFFFFFFFFL;
    public static final Long GENERATE_ID_U_64_LOCAL_MASK = 0x8000000000000000L;
    public static final LocalDateTime GLOBAL_MARK_START_COUNT_TIME_BY_GREGORIAN = LocalDateTime.of(1582, 10, 15, 0, 0, 0);

    public static final int PASSWORD_LENGTH = 12;

    public static class EmailSenderOpenApi {

      public static final String HOST_HEADER = "X-RapidAPI-Host";
      public static final String TOKEN_HEADER = "X-RapidAPI-Key";
    }
  }
}