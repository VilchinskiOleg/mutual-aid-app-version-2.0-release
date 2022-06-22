package org.tms.common.auth.configuration.provider;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tms.common.auth.configuration.basic_clients.BasicClient;

@Component
public class BasicAuthProvider implements AuthenticationProvider {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private Map<String, BasicClient> basicClients;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        var basicClient = basicClients.get(login);
        if (nonNull(basicClient) && passwordEncoder.matches(password, basicClient.getPassword())) {
            List<SimpleGrantedAuthority> roles = basicClient.getRoles().stream()
                                                                       .map(SimpleGrantedAuthority::new)
                                                                       .collect(toList());
            return new UsernamePasswordAuthenticationToken(basicClient.getName(), basicClient.getPassword(), roles);
        }
        throw new BadCredentialsException("Password is wrong");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}