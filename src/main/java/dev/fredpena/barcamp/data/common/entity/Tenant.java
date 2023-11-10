package dev.fredpena.barcamp.data.common.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "tenantId", callSuper = false)
@Table(name = "tenant", indexes = {@Index(columnList = "name")})
public class Tenant implements Serializable {

    @Id
    @NotNull
    private String tenantId;

    @NotNull
    @Column(length = 50)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 50)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant")
    private Set<TenantUser> users;

}
