package com.sheen.joe.bankingsystem.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getUserIdFromSecurityContext() {
        return getSecurityUserFromSecurityContext().getId();
    }

    public static String getUsernameFromSecurityContext() {
        return getSecurityUserFromSecurityContext().getUsername();
    }

    private static SecurityUser getSecurityUserFromSecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
    }

}
