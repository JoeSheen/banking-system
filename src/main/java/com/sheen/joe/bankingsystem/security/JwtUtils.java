package com.sheen.joe.bankingsystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sheen.joe.bankingsystem.exception.TokenVerificationException;
import com.sheen.joe.bankingsystem.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private static final String BEARER = "Bearer";

    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie requestCookie = WebUtils.getCookie(request, BEARER);
        if (requestCookie != null) {
            return requestCookie.getValue();
        }
        return null;
    }

    public String getUsernameSubject(String token) {
        return decodeToken(token).getSubject();
    }

    public String generateTokenCookie(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(15));

        Algorithm algorithm = Algorithm.HMAC256(secret);
        String jwt = JWT.create().withSubject(username).withIssuedAt(now)
                .withExpiresAt(expiresAt).sign(algorithm);

        return ResponseCookie.from(BEARER, jwt).path("/api").maxAge(Duration.ofMinutes(15))
                .httpOnly(true).build().toString();
    }

    public boolean validateToken(String token) {
        return !StringUtils.isNullOrEmpty(token) && decodeToken(token) != null;
    }

    private DecodedJWT decodeToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (JWTVerificationException exception) {
            throw new TokenVerificationException(exception.getMessage());
        }
    }

}
