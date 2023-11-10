package dev.fredpena.barcamp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;


@Slf4j
@Service
@RequiredArgsConstructor
//@ConditionalOnProperty(name = "flyway.enabled", matchIfMissing = true)
public class FlywayService {

    private final DataSource dataSource;

    public void initNewTenantSchema(String schema) {

        log.info("Doing tenant {} database migration", schema);

        Flyway tenantDbMigration = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/tenant", "classpath:db/migration/" + schema)
                .target(MigrationVersion.LATEST)
                .baselineOnMigrate(true)
                .schemas(schema)
                .load();
        tenantDbMigration.migrate();

        log.info("Tenant {} database migration complete", schema);
    }

    public void initMetadataSchema() {
        log.info("Doing common database migration");

        Flyway tenantDbMigration = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/common")
                .target(MigrationVersion.LATEST)
                .baselineOnMigrate(true)
                .schemas(CurrentTenantResolver.DEFAULT)
                .load();

        tenantDbMigration.migrate();
        log.info("Common database migration complete");
    }
}
