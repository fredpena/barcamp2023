package dev.fredpena.barcamp.security;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.tenant.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


@Getter
public class CustomUserDetails implements UserDetails {

    private final CommonUser user;
    private final Set<Tenant> tenants;
    private final boolean enabled;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(CommonUser user, Set<Tenant> tenants, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.tenants = tenants;
        this.enabled = enabled;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set. If the authority is null, it is a custom authority and should
            // precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }

    }
}
