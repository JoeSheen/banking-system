package com.sheen.joe.bankingsystem.security;

import com.sheen.joe.bankingsystem.entity.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static boolean isAuthenticated() {
        boolean authenticated = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (UserRole.isKnownAuthority(authority.getAuthority())) {
                    authenticated = true;
                    break;
                }
            }
        }
        return authenticated;
    }

    public static UUID getUserIdFromSecurityContext() {
        return getSecurityUserFromSecurityContext().getId();
    }

    private static SecurityUser getSecurityUserFromSecurityContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
    }

}
