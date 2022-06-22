package org.tms.authservicerest.domain.service;

import com.auth0.jwt.interfaces.Claim;
import java.util.Map;

/**
 * Interface for working with JWT token. Support create, verify token operations.
 */
public interface JwtHandler {

  /**
   * Create token by ticket as payload. Ticket should kep 'profileId' as field.
   *
   * @param ticket profile's details as payload.
   * @return jwt value.
   */
  String createToken(Map<String, Object> ticket);

  /**
   * Verify token structure and check sign of token.
   *
   * @param jwt token.
   * @return token is verified or not.
   */
  boolean verifyToken(String jwt);

  /**
   * Parse JWT token. Such method should be used only for trusted token.
   *
   * @param jwt should be verified before.
   * @return user ticket (payload of JWT).
   */
  Map<String, Claim> parseTicketFromTrustedToken(String jwt);
}