package org.tms.common.auth.configuration;

import static org.exception.handling.autoconfiguration.utils.Constant.UNAUTHORIZED_REQUEST;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.exception.handling.autoconfiguration.model.Error;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class FailAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

  @PostConstruct
  public void init() {
    setRealmName("failAuthenticationEntryPoint");
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    Error error= Error.builder()
            .code(UNAUTHORIZED_REQUEST)
            //TODO: make localised messages by code and write them to the json file OR more interesting handling:
            .message(ex.getMessage())
            .build();
    var responseBody = new BaseResponse(error);
    var responseBodyStr = new ObjectMapper().writeValueAsString(responseBody);
    response.getWriter().write(responseBodyStr);
  }

  @Override
  public String getRealmName() {
    return super.getRealmName();
  }

  @Override
  public void setRealmName(String realmName) {
    super.setRealmName(realmName);
  }
}