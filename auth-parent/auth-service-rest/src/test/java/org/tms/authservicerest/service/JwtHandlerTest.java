package org.tms.authservicerest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.tms.authservicerest.utils.ReadMockUtils.readMockFromFile;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.service.jwt.JwtHandler;
import org.tms.authservicerest.domain.service.jwt.JwtHandlerImp;
import org.tms.authservicerest.domain.service.jwt.JwtProperties;

@ContextConfiguration(classes = JwtHandlerImp.class)
@ExtendWith(SpringExtension.class)

public class JwtHandlerTest {

  private static final String MOCK_DATA_PATH = "mockdata/";
  private static final String ISSUER = "simple_issuer";
  private static final String SECRET = "simple_secret";
  private static final String WRONG_SECRET = "simple_wrong_secret";

  @Autowired
  private JwtHandler jwtHandler;
  @MockBean
  private JwtProperties jwtProperties;

  @BeforeEach
  void initEach() {
    when(jwtProperties.getIssuer()).thenReturn(ISSUER);
  }

  @SneakyThrows
  @Test
  void create_token_and_verify_successfully() {
    when(jwtProperties.getSecret()).thenReturn(SECRET);
    when(jwtProperties.getLive()).thenReturn(1L);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertTrue(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_dont_verify_if_secret_changed() {
    when(jwtProperties.getSecret()).thenReturn(SECRET);
    when(jwtProperties.getLive()).thenReturn(1L);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    when(jwtProperties.getSecret()).thenReturn(WRONG_SECRET);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_dont_verify_if_token_expired() {
    when(jwtProperties.getSecret()).thenReturn(SECRET);
    when(jwtProperties.getLive()).thenReturn(0L);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);

    String jwt = jwtHandler.createToken(profile);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @SneakyThrows
  @Test
  void create_token_and_parse_trusted_token_successfully() {
    when(jwtProperties.getSecret()).thenReturn(SECRET);
    when(jwtProperties.getLive()).thenReturn(1L);

    final var profile = readMockFromFile("Profile.json", MOCK_DATA_PATH, Profile.class);
    String checkingValue = profile.getResourceId();

    String jwt = jwtHandler.createToken(profile);
    assertTrue(jwtHandler.verifyToken(jwt));
    final var decodedTicket = jwtHandler.parseTicketFromTrustedToken(jwt);
    assertEquals(checkingValue, decodedTicket.get("resourceId").as(String.class));
  }
}