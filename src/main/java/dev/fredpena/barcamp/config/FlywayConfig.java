package dev.fredpena.barcamp.config;


import dev.fredpena.barcamp.data.common.entity.Tenant;
import dev.fredpena.barcamp.data.common.service.TenantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final FlywayService flywayService;
    private final TenantService tenantService;

    @PostConstruct
    public void migrateFlyway() {

        flywayService.initMetadataSchema();

        for (Tenant tenant : tenantService.findAll()) {
            flywayService.initNewTenantSchema(tenant.getTenantId());
        }

    }

}
