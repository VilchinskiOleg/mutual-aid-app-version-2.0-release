package org.tms.authservicerest.domain.service;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.temporal.ChronoUnit.MINUTES;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtHandlerImp implements JwtHandler {

  @Value("${auth.jwt.secret}")
  private static String SECRET;
  @Value("${auth.jwt.issuer}")
  private static String ISSUER;
  @Value("${auth.jwt.live}")
  private static long LIVE_TOKEN_PERIOD;

  @Override
  public String createToken(Map<String, Object> ticket) {
    Algorithm algorithm = Algorithm.HMAC256(SECRET);
    // Claim doesn't support LocalDateTime as type, so I convert it to String:
    ticket.put("expiredAt", now().plus(LIVE_TOKEN_PERIOD, MINUTES).toString());
    final String jwt = JWT.create()
        .withPayload(ticket)
        .withIssuer(ISSUER)
        .sign(algorithm);

    return jwt;
  }

  @Override
  public boolean verifyToken(String jwt) {
    Algorithm algorithm = Algorithm.HMAC256(SECRET);
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build();
    try {
      DecodedJWT decodedJWT = verifier.verify(jwt);
      var ticket = decodedJWT.getClaims();
      // Claim doesn't support LocalDateTime as type, so I parse it from String:
      var expiredAt = parse(ticket.get("expiredAt").as(String.class));
      if (now().isBefore(expiredAt)) {
        return true;
      }
      log.error("Token is expired. Access denied!");
    } catch (JWTDecodeException ex) {
      log.error("Cannot decode expiration date. Access denied!", ex);
    } catch (JWTVerificationException ex) {
      log.error("Sign of token is wrong. Access denied!", ex);
    }
    return false;
  }

  @Override
  public Map<String, Claim> parseTicketFromTrustedToken(String jwt) {
    try {
      return JWT.decode(jwt).getClaims();
    } catch (JWTDecodeException ex) {
      log.error("Unexpected error while parsing token. Should use only verified and trusted token!", ex);
      throw ex;
    }
  }
}