package org.tms.common.auth.configuration.filter;

import static java.util.Objects.nonNull;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tms.common.auth.configuration.model.JwtAuthenticationToken;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestTokenHeader = request.getHeader(AUTHORIZATION);

        if (nonNull(requestTokenHeader) && requestTokenHeader.startsWith(JWT_PREFIX)) {
            final var jwt = requestTokenHeader.substring(7);
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authorisation by JWT for request: {}", request);
        }

        filterChain.doFilter(request, response);
    }
}