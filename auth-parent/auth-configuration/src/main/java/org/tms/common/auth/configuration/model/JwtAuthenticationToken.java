package org.tms.common.auth.configuration.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;
    private Object credentials;

    private String jwt;
    @Setter
    private String profileId;

    public JwtAuthenticationToken(String jwt) {
        super( null);
        this.jwt = jwt;
    }

    @Override
    public void eraseCredentials() {
        this.jwt = null;
    }
}