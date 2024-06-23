package com.sheen.joe.bankingsystem.security;

import com.sheen.joe.bankingsystem.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
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

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        UUID id = UUID.fromString("b6934164-f725-4c56-a570-eef3119517fa");
        SecurityUser principal = new SecurityUser(id, Set.of(UserRole.USER_ROLE), "password", "WillHawks7Ft4q4");

        authentication = new TestingAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Test
    void testIsAuthenticatedReturnsFalse() {
        authentication = new TestingAuthenticationToken(null, null, "UNKNOWN_AUTHORITY");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();
        assertFalse(result);
    }

    @Test
    void testIsAuthenticatedReturnsTrue() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        boolean result = SecurityUtils.isAuthenticated();
        assertTrue(result);
    }

    @Test
    void testGetUserIdFromSecurityContext() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        UUID id = SecurityUtils.getUserIdFromSecurityContext();
        assertEquals(UUID.fromString("b6934164-f725-4c56-a570-eef3119517fa"), id);
    }
}
