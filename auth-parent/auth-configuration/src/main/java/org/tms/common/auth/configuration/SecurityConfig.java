package org.tms.common.auth.configuration;

import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tms.common.auth.configuration.filter.JwtAuthenticationFilter;
import org.tms.common.auth.configuration.provider.BasicAuthProvider;
import org.tms.common.auth.configuration.provider.JwtAuthProvider;

@ComponentScan("org.tms.common.auth.configuration")
//@PropertySource("classpath:auth-props.yml") -> just for example, like how to can add your own source properties.

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private BasicAuthProvider basicAuthProvider;
  @Resource
  private JwtAuthProvider jwtAuthProvider;
  @Resource
  private FailAuthenticationEntryPoint failAuthenticationEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // turn off http session ?

    http.authorizeRequests()
        .antMatchers("api/auth-service/**").permitAll()
        .anyRequest().authenticated();

    http.httpBasic().authenticationEntryPoint(failAuthenticationEntryPoint);

    http.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //todo: check with out my filter *
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .authenticationProvider(basicAuthProvider)
        .authenticationProvider(jwtAuthProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}