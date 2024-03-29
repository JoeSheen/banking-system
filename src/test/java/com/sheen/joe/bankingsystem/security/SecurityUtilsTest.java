package com.sheen.joe.bankingsystem.security;

import com.sheen.joe.bankingsystem.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUtilsTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private SecurityUser principal;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        UUID id = UUID.fromString("b6934164-f725-4c56-a570-eef3119517fa");
        principal = new SecurityUser(id, Set.of(UserRole.USER_ROLE), "password", "WillHawks7Ft4q4");
    }

    @Test
    void testGetUserIdFromSecurityContext() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);

        UUID id = SecurityUtils.getUserIdFromSecurityContext();
        assertEquals(UUID.fromString("b6934164-f725-4c56-a570-eef3119517fa"), id);
    }
}
