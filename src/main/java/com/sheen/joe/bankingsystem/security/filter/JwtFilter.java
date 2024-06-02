package com.sheen.joe.bankingsystem.security.filter;

import com.sheen.joe.bankingsystem.exception.TokenVerificationException;
import com.sheen.joe.bankingsystem.exception.handler.HandlerResponse;
import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String jwt = jwtUtils.getTokenFromRequest(request);
            if (jwtUtils.validateToken(jwt)) {
                final String username = jwtUtils.getUsernameSubject(jwt);
                if (isUserUnauthenticated(username)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    setSecurityAuthentication(token);
                }
            }
        } catch (TokenVerificationException e) {
            handleTokenVerificationException(response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isUserUnauthenticated(String username) {
        return !StringUtils.isNullOrEmpty(username) && getSecurityContext().getAuthentication() == null;
    }

    private void setSecurityAuthentication(Authentication authentication) {
        getSecurityContext().setAuthentication(authentication);
    }

    private SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    private void handleTokenVerificationException(HttpServletResponse response, TokenVerificationException exception)
            throws IOException {
        String message = exception.getMessage();
        LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("UTC"));
        HandlerResponse handlerResponse = new HandlerResponse(message, HttpStatus.UNAUTHORIZED, timestamp);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.valueOf(handlerResponse));
    }

}
