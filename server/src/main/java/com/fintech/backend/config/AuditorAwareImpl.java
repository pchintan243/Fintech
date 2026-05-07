package com.fintech.backend.config;

import com.fintech.backend.entity.User;
import com.fintech.backend.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the current logged-in user's ID for Spring Data JPA Auditing.
 *
 * Returns the UserIDP (Long) of the authenticated user so that
 * createdByIdf and lastModifiedByIdf are auto-populated.
 * Returns Optional.empty() when there is no authenticated user
 * (e.g. during anonymous/registration requests).
 */
@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final UserRepository userRepository;

    public AuditorAwareImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(user -> user.getId());
    }
}
