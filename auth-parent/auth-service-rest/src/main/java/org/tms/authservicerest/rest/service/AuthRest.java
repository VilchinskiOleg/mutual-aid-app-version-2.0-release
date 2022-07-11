package org.tms.authservicerest.rest.service;

import com.auth0.jwt.interfaces.Claim;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.domain.service.jwt.JwtHandler;
import org.tms.authservicerest.domain.service.ProfileManagerService;
import org.tms.authservicerest.rest.message.req.JwtRequest;
import org.tms.authservicerest.rest.message.req.LoginProfileRequest;
import org.tms.authservicerest.rest.message.res.CreateProfileResponse;
import org.tms.authservicerest.rest.message.res.JwtResponse;
import org.tms.authservicerest.rest.message.req.CreateProfileRequest;
import org.tms.authservicerest.rest.message.res.VerifyJWTResponse;

@RestController
@RequestMapping(path = "api/auth-service")
public class AuthRest {

  @Resource
  private JwtHandler jwtHandler;
  @Resource
  private ProfileManagerService profileManagerService;
  @Resource
  private Mapper mapper;

  @Api
  @ApiOperation(value = "${auth.operation.create}")
  @PostMapping("/create")
  @PreAuthorize("hasRole('CREATE_AUTH_PROFILE')")
  public CreateProfileResponse create(@RequestBody CreateProfileRequest request) {
    var profile = profileManagerService.create(mapper.map(request.getProfile(), Profile.class));
    var createdProfile = mapper.map(profile, org.tms.authservicerest.rest.model.Profile.class);
    return new CreateProfileResponse(createdProfile);
  }

  @Api
  @ApiOperation(value = "${auth.operation.login}")
  @PostMapping("/login")
  public JwtResponse login(@RequestBody LoginProfileRequest request) {
    var jwt = profileManagerService.login(mapper.map(request.getTicket(), Ticket.class));
    return new JwtResponse(jwt);
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
      response.setProfileId(ticket.get("resourceId").as(String.class));
    }
    return response;
  }
}