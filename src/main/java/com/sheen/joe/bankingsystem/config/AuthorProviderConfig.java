package com.sheen.joe.bankingsystem.config;

import com.sheen.joe.bankingsystem.security.SecurityUtils;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorProviderConfig {

    @Bean
    public AuthorProvider authorProvider() {
        return () -> {
            boolean authenticated = SecurityUtils.isAuthenticated();
            if (!authenticated) {
                return "System Commit";
            }
            return SecurityUtils.getUserIdFromSecurityContext().toString();
        };
    }
}
