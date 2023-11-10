package dev.fredpena.barcamp.data.common.service;

import dev.fredpena.barcamp.data.common.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TenantRepository extends JpaRepository<Tenant, String>, JpaSpecificationExecutor<Tenant> {

    @Query(value = "select t.* from tenant t " +
                   "inner join tenant_user tu on t.tenant_id = tu.tenant_id " +
                   "inner join common_user ut on tu.username = ut.username " +
                   "where tu.username = :username and tu.disabled = false", nativeQuery = true)
    Set<Tenant> findByUsername(@Param("username") String username);
}
