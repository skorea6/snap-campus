package com.example.snapcampus.config;

import com.example.snapcampus.dto.security.SnapPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(
                SecurityContextHolder.getContext()
        ).map(SecurityContext::getAuthentication)
                .filter(it -> it.isAuthenticated() && !it.getName().equals("anonymousUser"))
                .map(Authentication::getPrincipal)
                .map(SnapPrincipal.class::cast) // x -> (BoardPrincipal) x
                .map(SnapPrincipal::getUsername);
    }
}
