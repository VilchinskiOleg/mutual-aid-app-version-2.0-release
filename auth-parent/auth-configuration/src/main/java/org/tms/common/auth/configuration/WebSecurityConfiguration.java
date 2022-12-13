package org.tms.common.auth.configuration;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tms.common.auth.configuration.basic_clients.BasicClient;
import org.tms.common.auth.configuration.client.AuthRestClientService;
import org.tms.common.auth.configuration.filter.JwtAuthenticationFilter;
import org.tms.common.auth.configuration.provider.BasicAuthProvider;
import org.tms.common.auth.configuration.provider.JwtAuthProvider;
import java.util.Map;

@ComponentScan("org.tms.common.auth.configuration")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  private static final String[] AUTH_WHITE_LIST = {
      // -- Swagger UI v2:
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/webjars/**",
      // -- Swagger UI v3 (OpenAPI):
      "/v3/api-docs/**",
      "/swagger-ui/**",
      // -- Auth Service:
      "/api/auth-service/login",
      "/api/auth-service/verify-token"
  };


  /**
   * If this is starter inside AuthRest -> we don't need any authClientServices. Look at AuthRestClientService class definition:
   */
  @Autowired(required = false)
  private AuthRestClientService authClientService;
  @Resource
  private FailAuthenticationEntryPoint failAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
                                         PasswordEncoder passwordEncoder,
                                         Map<String, BasicClient> basicClients) throws Exception {
    /**
     * Configure security:
     */
    http.csrf().disable();
    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // turn off http session, because check JWT token
    http.authorizeRequests()
            .antMatchers(AUTH_WHITE_LIST).permitAll()
            .anyRequest().authenticated();
    http.httpBasic().authenticationEntryPoint(failAuthenticationEntryPoint);
    http.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //todo: check with out my filter *
    /**
     * Add provider to check authentication:
     */
    http.authenticationProvider(new BasicAuthProvider(passwordEncoder, basicClients));
    http.authenticationProvider(new JwtAuthProvider(authClientService));

    return http.build();
  }

  @Bean
  @Lazy
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}