package org.tms.common.auth.configuration.global;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JacksonObjectMapperGlobalTest {

  private static final String JWT_MOCK = "asdadada.sdfsdfgsd.sdfsds";
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @SneakyThrows
  void serializationAndDeserialization_ReqWithJsonProp() {

    var req = new JwtRequestWithJsonProp(JWT_MOCK);

    String serializedValue = objectMapper.writeValueAsString(req);
    log.info("serialized object as string, if we use '@JsonProperty(\"jwt_value\")' : {}", serializedValue);

    var deserializedValueAsMap = objectMapper.readValue(serializedValue, new TypeReference<Map<String, Object>>(){});
    assertNotNull(deserializedValueAsMap.get("jwt_value"));
    var deserializedValueAsParticularTargetClass = objectMapper.readValue(serializedValue, JwtRequestWithJsonProp.class);
    assertNotNull(deserializedValueAsParticularTargetClass.getJwt());
  }

  @Test
  @SneakyThrows
  void serializationAndDeserialization_ReqWithJsonAlias() {

    var req = new JwtRequestWithJsonAlias(JWT_MOCK);

    String serializedValue = objectMapper.writeValueAsString(req);
    log.info("serialized object as string, if we use '@JsonAlias(\"jwt_value\")' : {}", serializedValue);

    var deserializedValueAsMap = objectMapper.readValue(serializedValue, new TypeReference<Map<String, Object>>(){});
    assertNull(deserializedValueAsMap.get("jwt_value"));
    var deserializedValueAsParticularTargetClass = objectMapper.readValue(serializedValue, JwtRequestWithJsonAlias.class);
    assertNotNull(deserializedValueAsParticularTargetClass.getJwt());
  }

  @Test
  @SneakyThrows
  void throwException_ifNotFindNecessaryPojoField() {
    final String jsonStr = "{\"wrongField\":\"asdadada.sdfsdfgsd.sdfsds\"}";
    assertThrows(JsonMappingException.class, () -> objectMapper.readValue(jsonStr, JwtRequestSimple.class));
  }

  @Test
  @SneakyThrows
  void skipExtraJsonField_ifPojoDoesntHaveThem() {
    var customisedMapper = new ObjectMapper();
    // Override default behavior:
    customisedMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    final String jsonStr = "{\"jwt\":\"asdadada.sdfsdfgsd.sdfsds\", \"extraField\":\"asdadada.sdfsdfgsd.sdfsds\"}";
    var result = customisedMapper.readValue(jsonStr, JwtRequestSimple.class);
    assertNotNull(result.getJwt());
  }

  @AllArgsConstructor
  @NoArgsConstructor
  private static class JwtRequestWithJsonProp {

    @Getter
    @Setter

    @JsonProperty("jwt_value")
    private String jwt;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  private static class JwtRequestWithJsonAlias {

    @Getter
    @Setter

    @JsonAlias("jwt_value")
    private String jwt;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  private static class JwtRequestSimple {

    @Getter
    @Setter
    private String jwt;
  }
}