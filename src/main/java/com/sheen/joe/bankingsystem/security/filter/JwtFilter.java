package com.sheen.joe.bankingsystem.security.filter;

import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;

    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTH_HEADER);
        if (isHeaderValid(authHeader)) {
            final String jwt = jwtUtils.getToken(authHeader);
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
        }
        filterChain.doFilter(request, response);
    }

    private boolean isHeaderValid(String header) {
        return !StringUtils.isNullOrEmpty(header) && header.contains(BEARER);
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

}
