package dev.fredpena.barcamp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    public static final String DEFAULT = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        if (StringUtils.hasText(tenant)) {
            log.info("Schema: {}", tenant);
            return tenant;
        }
        log.info("Schema: {}", DEFAULT);
        return DEFAULT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

}
