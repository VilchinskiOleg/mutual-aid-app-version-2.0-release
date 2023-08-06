package org.tms.common.auth.configuration.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tms.common.auth.configuration.model.JwtAuthenticationToken;

public class AuthenticationUtils {

    public static String fetchAuthorOfRequestUserIdFromAuthContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) auth).getProfileId();
        }
        // If you call that method from TEST account (not like usual user), method will write your login as a authorId:
        return auth.getPrincipal().toString();
    }
}