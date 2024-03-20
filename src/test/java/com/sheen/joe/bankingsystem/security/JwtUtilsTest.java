package com.sheen.joe.bankingsystem.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Spy
    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        String secret = "secret";
        ReflectionTestUtils.setField(jwtUtils, "secret", secret);
    }

    @Test
    void testGetToken() {
        String token = jwtUtils.getToken("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJNYXJpYURhdmlzMUEyTlo3IiwiaWF0IjoxNzEwODc5NTQ1LCJleHAiOjE3MTA4ODAxNDV9." +
                "uCSJWUqWW8QR0s7QFgOd9H_o31rch3B35-dpMkhIrfI");

        assertNotNull(token);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJNYXJpYURhdmlzMUEyTlo3IiwiaWF0IjoxNzEwODc5NTQ1LCJleHAiOjE3MTA4ODAxNDV9." +
                "uCSJWUqWW8QR0s7QFgOd9H_o31rch3B35-dpMkhIrfI", token);
    }

    @Test
    void testGetUsernameSubject() {
        String token = buildTokenForTest();

        String username = jwtUtils.getUsernameSubject(token);
        assertEquals("MariaDavis1A2NZ7", username);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtils.generateToken("MariaDavis1A2NZ7");

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void testValidateToken() {
        String token = buildTokenForTest();

        boolean isValid = jwtUtils.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        boolean isValid = jwtUtils.validateToken("");
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenThrowsTokenExpiredException() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJNYXJpYURhdmlzMUEyTlo3Iiw" +
                "iaWF0IjoxNzEwODgwMDMwLCJleHAiOjE3MTA4ODA2MzB9.P3o7xs0e-0eIBr0-wFcuaOYODOEkXIxqftjkPnuK97E";

        JWTVerificationException exception = assertThrows(JWTVerificationException.class, () ->
                jwtUtils.validateToken(token));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Failed to verify JWT: The Token has expired on 2024-03-19T20:37:10Z";

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private String buildTokenForTest() {
        return jwtUtils.generateToken("MariaDavis1A2NZ7");
    }
}
