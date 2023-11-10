package dev.fredpena.barcamp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectionProvider implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    private final transient DataSource dataSource;


    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection(CurrentTenantResolver.DEFAULT);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        log.trace("Get connection for tenant '{}'", tenantId);
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantId);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantId, Connection connection) throws SQLException {
        log.trace("Release connection for tenant '{}'", tenantId);
        connection.setSchema(CurrentTenantResolver.DEFAULT);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
