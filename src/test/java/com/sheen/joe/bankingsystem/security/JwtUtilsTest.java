package com.sheen.joe.bankingsystem.security;

import com.sheen.joe.bankingsystem.exception.TokenVerificationException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
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
    void testGetTokenFromRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJNYXJpYURhdmlzMUEyTlo3IiwiaWF0IjoxNzEwODc5NTQ1LCJleHAiOjE3MTA4ODAxNDV9." +
                "uCSJWUqWW8QR0s7QFgOd9H_o31rch3B35-dpMkhIrfI"));
        String token = jwtUtils.getTokenFromRequest(request);


        assertNotNull(token);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJNYXJpYURhdmlzMUEyTlo3IiwiaWF0IjoxNzEwODc5NTQ1LCJleHAiOjE3MTA4ODAxNDV9." +
                "uCSJWUqWW8QR0s7QFgOd9H_o31rch3B35-dpMkhIrfI", token);
    }

    @Test
    void testGetUsernameSubject() {
        String tokenCookie = buildTokenForTest();
        String token = tokenCookie.substring(7, tokenCookie.indexOf(";"));

        String username = jwtUtils.getUsernameSubject(token);
        assertEquals("MariaDavis1A2NZ7", username);
    }

    @Test
    void testGenerateTokenCookie() {
        String token = jwtUtils.generateTokenCookie("MariaDavis1A2NZ7");

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer="));
    }

    @Test
    void testValidateToken() {
        String tokenCookie = buildTokenForTest();
        String token = tokenCookie.substring(7, tokenCookie.indexOf(";"));

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

        TokenVerificationException exception = assertThrows(TokenVerificationException.class, () ->
                jwtUtils.validateToken(token));

        String actualMessage = exception.getMessage();
        String expectedMessage = "The Token has expired on 2024-03-19T20:37:10Z";

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private String buildTokenForTest() {
        return jwtUtils.generateTokenCookie("MariaDavis1A2NZ7");
    }
}
