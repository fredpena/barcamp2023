package dev.fredpena.barcamp.security.auditable;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import dev.fredpena.barcamp.security.CustomUserDetails;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(p -> (CustomUserDetails) p)
                .map(CustomUserDetails::getUser)
                .map(CommonUser::getUsername);
    }
}