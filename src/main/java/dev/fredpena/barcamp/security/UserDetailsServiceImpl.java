package dev.fredpena.barcamp.security;

import dev.fredpena.barcamp.data.common.entity.CommonUser;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.common.service.CommonUserService;
import dev.fredpena.barcamp.data.common.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CommonUserService commonUserService;
    private final TenantService tenantService;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CommonUser user = commonUserService.findUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        }

        Set<Tenant> tenants = tenantService.findTenantByUser(user.getUsername());
        if (tenants == null || tenants.isEmpty()) {
            throw new UsernameNotFoundException("No tenant present with username: " + username);
        }

        log.info("User information: {}", user);
        log.info("Tenant present: {}", tenants.size());

        return new CustomUserDetails(user, tenants, true, getAuthorities(user));
    }

    private static List<GrantedAuthority> getAuthorities(CommonUser user) {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

    }

}
