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
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String name;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String slogan;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String type;


    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String phone;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String email;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String website;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String address;

    @NotNull
    @Column(length = 100)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 100)
    private String logo;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant")
    private Set<TenantUser> users;

}
