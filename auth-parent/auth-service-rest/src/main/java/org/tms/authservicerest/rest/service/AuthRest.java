package org.tms.authservicerest.rest.service;

import com.auth0.jwt.interfaces.Claim;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.common.http.autoconfiguration.annotation.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tms.authservicerest.domain.service.JwtHandler;
import org.tms.authservicerest.rest.message.req.JwtRequest;
import org.tms.authservicerest.rest.message.res.JwtResponse;
import org.tms.authservicerest.rest.message.req.ProfileRequest;
import org.tms.authservicerest.rest.message.res.RegisterProfileResponse;
import org.tms.authservicerest.rest.message.res.VerifyJWTResponse;

@RestController
@RequestMapping(path = "api/auth-service")
public class AuthRest {

  @Resource
  private JwtHandler jwtHandler;

  @Api
  @ApiOperation(value = "${auth.operation.create}")
  @PostMapping("/create")
  public RegisterProfileResponse register(@RequestBody ProfileRequest request) {

    return null;
  }

  @Api
  @ApiOperation(value = "${auth.operation.login}")
  @PostMapping("/login")
  public JwtResponse login(@RequestBody ProfileRequest request) {

    return null;
  }

  @Api
  @ApiOperation(value = "${auth.operation.verify-token}")
  @PostMapping("/verify-token")
  public VerifyJWTResponse verifyToken(@RequestBody JwtRequest request) {
    final var response = new VerifyJWTResponse();
    final String jwt = request.getJwt();

    response.setSuccess(jwtHandler.verifyToken(jwt));
    if (response.getSuccess()) {
      Map<String, Claim> ticket = jwtHandler.parseTicketFromTrustedToken(jwt);
      response.setProfileId(ticket.get("profileId").as(String.class));
    }
    return response;
  }
}