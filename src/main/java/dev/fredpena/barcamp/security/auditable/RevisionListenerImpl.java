package dev.fredpena.barcamp.security.auditable;

import dev.fredpena.barcamp.data.tenant.entity.Revision;
import dev.fredpena.barcamp.security.CustomUserDetails;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

public class RevisionListenerImpl implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        final SecurityContext context = SecurityContextHolder.getContext();
        Objects.requireNonNull(context, "Context cannot be null");

        final Authentication authentication = context.getAuthentication();
        Objects.requireNonNull(authentication, "Authentication cannot be null");

        if (authentication.isAuthenticated()) {
            CustomUserDetails ul = (CustomUserDetails) authentication.getPrincipal();
            final String ipAddress = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();

            Revision revision = (Revision) revisionEntity;
            revision.setModifierUser(ul.getUsername());
            revision.setIpAddress(ipAddress);
        }
    }
}