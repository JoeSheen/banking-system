package com.sheen.joe.bankingsystem.entity;

import org.springframework.security.core.GrantedAuthority;

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

}
