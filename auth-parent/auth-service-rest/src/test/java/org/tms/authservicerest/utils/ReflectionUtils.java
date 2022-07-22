package org.tms.authservicerest.utils;

import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionUtils {

  public static void setCustomConstantValue(Object subject, String fName, Object fValue) {
    try {
      Field secret = subject.getClass().getDeclaredField(fName);
      secret.setAccessible(true);
      secret.set(subject, fValue);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      log.error("Unexpected error while setting value to constant field = {}.", fName, ex);
      throw new RuntimeException(ex);
    }
  }
}