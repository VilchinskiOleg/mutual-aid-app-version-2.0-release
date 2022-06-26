package org.tms.common.auth.configuration;

import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
      throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    PrintWriter writer = response.getWriter();
    writer.println("HTTP Status 401 : Unauthorized -> " + authEx.getMessage());
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