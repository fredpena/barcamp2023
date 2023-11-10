package dev.fredpena.barcamp.security;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;
import dev.fredpena.barcamp.config.TenantContext;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.tenant.entity.User;
import dev.fredpena.barcamp.data.tenant.service.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;


    @Transactional
    public Optional<User> get() {
        if (!StringUtils.hasText(TenantContext.getCurrentTenant())) {
            return Optional.empty();
        }

        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        if (currentUser != null) {
            return Optional.of(currentUser);
        }

        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(ud -> (CustomUserDetails ) ud)
                .map(ul -> {
                    final User user = userRepository.findByUsername(ul.getUsername());
                    log.info("Authenticate: userid = = {}", user.getUserId());

                    user.setLastLoginTs(Instant.now());
                    userRepository.save(user);

                    VaadinSession.getCurrent().setAttribute(User.class, user);

                    return user;
                });
    }

    public Optional<Boolean> userHasSomeTenant() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(ud -> (CustomUserDetails ) ud)
                .map(ul -> ul.getTenants().size() > 1);
    }

    public void clearSession() {
        VaadinSession.getCurrent().setAttribute(Tenant.class, null);
        VaadinSession.getCurrent().setAttribute(User.class, null);
    }

    public void logout() {
        authenticationContext.logout();
    }

}
