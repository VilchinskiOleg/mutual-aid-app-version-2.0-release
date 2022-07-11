package org.tms.authservicerest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tms.authservicerest.utils.ReadMockUtils.readMockFromFile;
import static org.tms.authservicerest.utils.ReflectionUtils.setCustomConstantValue;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.service.jwt.JwtHandler;
import org.tms.authservicerest.domain.service.jwt.JwtHandlerImp;

@ContextConfiguration(classes = JwtHandlerImp.class)
@ExtendWith(SpringExtension.class)

public class JwtHandlerTest {

  private static final String MOCK_DATA_PATH = "mockdata/";

  @Autowired
  private JwtHandler jwtHandler;

  @BeforeEach
  void initAll() {
    setCustomConstantValue(jwtHandler, "ISSUER", "simple_issuer");
  }

  @SneakyThrows
  @Test
  void create_token_and_verify_successfully() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 1);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertTrue(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_dont_verify_if_secret_changed() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 1);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    setCustomConstantValue(jwtHandler, "SECRET", "simple_other_secret");
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_dont_verify_if_token_expired() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 0);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_parse_trusted_token_successfully() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 0);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);
    String checkingValue = profile.getResourceId();

    String jwt = jwtHandler.createToken(profile);
    final var decodedTicket = jwtHandler.parseTicketFromTrustedToken(jwt);

    //verify
    assertEquals(checkingValue, decodedTicket.get("resourceId").as(String.class));
  }
}