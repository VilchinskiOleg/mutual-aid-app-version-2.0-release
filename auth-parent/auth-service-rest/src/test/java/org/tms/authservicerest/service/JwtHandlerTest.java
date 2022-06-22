package org.tms.authservicerest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tms.authservicerest.utils.ReflectionUtils.setCustomConstantValue;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.authservicerest.domain.service.JwtHandler;
import org.tms.authservicerest.domain.service.JwtHandlerImp;

@ContextConfiguration(classes = JwtHandlerImp.class)
@ExtendWith(SpringExtension.class)

public class JwtHandlerTest {

  @Autowired
  private JwtHandler jwtHandler;

  @BeforeEach
  void initAll() {
    setCustomConstantValue(jwtHandler, "ISSUER", "simple_issuer");
  }

  @Test
  void create_token_and_verify_successfully() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 1);
    final var ticket = new HashMap<String, Object>();

    ticket.put("profileId", "123");
    ticket.put("login", "login");

    String jwt = jwtHandler.createToken(ticket);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertTrue(verified);
  }

  @Test
  void create_token_and_dont_verify_if_secret_changed() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 1);
    final var ticket = new HashMap<String, Object>();

    ticket.put("profileId", "123");
    ticket.put("login", "login");

    String jwt = jwtHandler.createToken(ticket);
    setCustomConstantValue(jwtHandler, "SECRET", "simple_other_secret");
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @Test
  void create_token_and_dont_verify_if_token_expired() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 0);
    final var ticket = new HashMap<String, Object>();

    ticket.put("profileId", "123");
    ticket.put("login", "login");

    String jwt = jwtHandler.createToken(ticket);
    boolean verified = jwtHandler.verifyToken(jwt);

    //verify
    assertFalse(verified);
  }

  @Test
  void create_token_and_parse_trusted_token_successfully() {
    setCustomConstantValue(jwtHandler, "SECRET", "simple_secret");
    setCustomConstantValue(jwtHandler, "LIVE_TOKEN_PERIOD", 0);
    final var ticket = new HashMap<String, Object>();

    String checkingValue = "123";
    ticket.put("profileId", checkingValue);
    ticket.put("login", "login");

    String jwt = jwtHandler.createToken(ticket);
    final var decodedTicket = jwtHandler.parseTicketFromTrustedToken(jwt);

    //verify
    assertEquals(checkingValue, decodedTicket.get("profileId").as(String.class));
  }
}