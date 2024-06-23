package com.sheen.joe.bankingsystem.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Optional;

public enum UserRole implements GrantedAuthority {

    USER_ROLE("USER_ROLE"),

    ADMIN_ROLE("ADMIN_ROLE");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static boolean isKnownAuthority(String authority) {
        Optional<UserRole> userRoleOptional = Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.authority.equals(authority)).findFirst();
        return userRoleOptional.isPresent();
    }

}
