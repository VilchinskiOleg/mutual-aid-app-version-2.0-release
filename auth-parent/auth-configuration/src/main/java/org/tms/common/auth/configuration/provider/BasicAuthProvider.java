package org.tms.common.auth.configuration.provider;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tms.common.auth.configuration.basic_clients.BasicClient;

public class BasicAuthProvider implements AuthenticationProvider {

    public static final String AUTHORITY_PREFIX = "ROLE_";
    private final PasswordEncoder passwordEncoder;
    private final Map<String, BasicClient> basicClients;

    public BasicAuthProvider(PasswordEncoder passwordEncoder, Map<String, BasicClient> basicClients) {
        this.passwordEncoder = passwordEncoder;
        this.basicClients = basicClients;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        var basicClient = basicClients.get(login);
        if (nonNull(basicClient) && passwordEncoder.matches(password, basicClient.getPassword())) {
            List<SimpleGrantedAuthority> roles = basicClient.getRoles().stream()
                                                                       .map(role -> new SimpleGrantedAuthority(AUTHORITY_PREFIX.concat(role)))
                                                                       .collect(toList());
            return new UsernamePasswordAuthenticationToken(basicClient.getName(), null, roles);
        }
        throw new BadCredentialsException("Login or Password is wrong!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}