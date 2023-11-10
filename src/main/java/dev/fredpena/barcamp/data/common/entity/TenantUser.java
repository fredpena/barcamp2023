package dev.fredpena.barcamp.data.common.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tenant_user")
public class TenantUser implements Serializable {

    @EmbeddedId
    private TenantUserKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tenantId")
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    private CommonUser user;

    private boolean disabled;


}
