package com.sheen.joe.bankingsystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sheen.joe.bankingsystem.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    public String getToken(String authenticationHeader) {
        return authenticationHeader.substring(7);
    }

    public String getUsernameSubject(String token) {
        return decodeToken(token).getSubject();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(10));

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withSubject(username).withIssuedAt(now)
                .withExpiresAt(expiresAt).sign(algorithm);
    }

    public boolean validateToken(String token) {
        return !StringUtils.isNullOrEmpty(token) && decodeToken(token) != null;
    }

    private DecodedJWT decodeToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Failed to verify JWT: " + exception.getMessage());
        }
    }

}
