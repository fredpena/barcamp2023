package dev.fredpena.barcamp.data.tenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    protected String createdBy;

    @LastModifiedBy
    @Column(nullable = false)
    protected String lastModifiedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    protected Instant lastModifiedDate;

}