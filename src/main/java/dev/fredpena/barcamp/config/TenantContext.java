package dev.fredpena.barcamp.config;

import com.vaadin.flow.server.VaadinSession;
import dev.fredpena.barcamp.data.common.entity.Tenant;
import org.springframework.util.StringUtils;

public final class TenantContext {

    private TenantContext() {
    }

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private static final ThreadLocal<Long> currentTenantId = new ThreadLocal<>();


    public static String getCurrentTenant() {
        if (VaadinSession.getCurrent() != null) {
            Tenant tenant = VaadinSession.getCurrent().getAttribute(Tenant.class);
            if (tenant != null && StringUtils.hasText(tenant.getTenantId())) {
                return tenant.getTenantId();
            }
        }

        return currentTenant.get();
    }

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static Long getCurrentTenantId() {
        return currentTenantId.get();
    }

    public static void setCurrentTenantId(Long tenantId) {
        currentTenantId.set(tenantId);
    }

    public static void clear() {
        currentTenant.remove();
        currentTenantId.remove();
    }

}
