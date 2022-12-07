package org.tms.common.auth.configuration.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.tms.common.auth.configuration.client.AuthRestClientService;
import org.tms.common.auth.configuration.model.JwtAuthenticationToken;

@Slf4j
@Component
public class JwtAuthProvider implements AuthenticationProvider {

    /**
     * If this is starter inside AuthRest -> we don't need any authClientServices. Look at AuthRestClientService class definition:
     */
    @Autowired(required = false)
    private AuthRestClientService authClientService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String jwt = jwtAuthenticationToken.getJwt();

        var response = authClientService.verifyJwt(jwt);
        if (!response.isSuccess()) {
            throw new BadCredentialsException("JWT token unverified!");
        }

        jwtAuthenticationToken.setAuthenticated(true);
        jwtAuthenticationToken.setProfileId(response.getProfileId());
        jwtAuthenticationToken.eraseCredentials();
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}