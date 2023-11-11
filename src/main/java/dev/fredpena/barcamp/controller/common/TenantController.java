package dev.fredpena.barcamp.controller.common;

import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.common.service.CommonUserService;
import dev.fredpena.barcamp.data.common.service.TenantService;
import dev.fredpena.barcamp.security.CustomUserDetails;
import dev.fredpena.barcamp.security.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final JwtUtils jwtUtils;
    private final TenantService tenantService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Set<Tenant>> get() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();

        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(Set.of(), HttpStatus.UNAUTHORIZED);
        }

        CustomUserDetails ul = (CustomUserDetails) authentication.getPrincipal();
        Set<Tenant> tenants = tenantService.findTenantByUser(ul.getUsername());

        if (tenants == null || tenants.isEmpty()) {
            return new ResponseEntity<>(Set.of(), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(tenants);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<String> post(@RequestBody @Valid @NotNull TenantLogin tenantLogin) {

        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();

        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not login. ", HttpStatus.UNAUTHORIZED);
        }

        CustomUserDetails ul = (CustomUserDetails) authentication.getPrincipal();
        Set<Tenant> tenants = tenantService.findTenantByUser(ul.getUsername());

        if (tenants == null || tenants.isEmpty()) {
            return new ResponseEntity<>("Tenant not found: ", HttpStatus.NOT_FOUND);
        }

        if (tenants.stream().map(Tenant::getTenantId).noneMatch(p -> p.equals(tenantLogin.getTenantId()))) {
            return new ResponseEntity<>("Tenant not match: ", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(jwtUtils.issueToken(tenantLogin.getTenantId(), 0L, ul.getUsername()));

    }
}