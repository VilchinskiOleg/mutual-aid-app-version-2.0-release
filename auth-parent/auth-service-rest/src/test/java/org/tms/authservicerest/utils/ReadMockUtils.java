package org.tms.authservicerest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReadMockUtils {

  public static <T> T readMockFromFile(String fileName, String path, Class<T> resultType) throws java.io.IOException {
    final var objectMapper = new ObjectMapper();
    final var subUrl = path + fileName;
    Resource res = new ClassPathResource(subUrl);
    URL url = res.getURL();
    return objectMapper.readValue(url, resultType);
  }
}