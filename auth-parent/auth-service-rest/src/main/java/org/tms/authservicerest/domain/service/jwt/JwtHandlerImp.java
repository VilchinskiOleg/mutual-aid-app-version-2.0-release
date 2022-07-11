package org.tms.authservicerest.domain.service.jwt;

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
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;

@Slf4j
@Component
public class JwtHandlerImp implements JwtHandler {

  private static final String EXPIRED_AT_KEY = "expiredAt";

  @Resource
  private JwtProperties jwtProperties;

  @Override
  public String createToken(Profile profile) {
    // Claim doesn't support LocalDateTime as type, so I convert it to String:
    final var expiredAt = now().plus(jwtProperties.getLive(), MINUTES).toString();
    Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    Map<String, Object> jwtPayload = createPayload(profile);
    jwtPayload.put(EXPIRED_AT_KEY, expiredAt);
    final String jwt = JWT.create()
        .withPayload(jwtPayload)
        .withIssuer(jwtProperties.getIssuer())
        .sign(algorithm);

    return jwt;
  }

  @Override
  public boolean verifyToken(String jwt) {
    Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer(jwtProperties.getIssuer())
        .build();
    try {
      DecodedJWT decodedJWT = verifier.verify(jwt);
      var ticket = decodedJWT.getClaims();
      // Claim doesn't support LocalDateTime as type, so I parse it from String:
      var expiredAt = parse(ticket.get(EXPIRED_AT_KEY).as(String.class));
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

  private Map<String, Object> createPayload(Profile profile) {
    var jwtPayload = new HashMap<String, Object>();
    jwtPayload.put("resourceId", profile.getResourceId());
    jwtPayload.put("gender", profile.getGender().toString());
    //...
    return jwtPayload;
  }
}