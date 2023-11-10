package dev.fredpena.barcamp.data.common.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class TenantUserKey implements Serializable {

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "username")
    private String username;

}
