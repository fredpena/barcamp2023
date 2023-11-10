package dev.fredpena.barcamp.data.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;


@Setter
@Getter
@Entity(name = "common_user")
public class CommonUser {

    @Id
    @NotNull
    @Column(length = 30, unique = true, updatable = false)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 30)
    private String username;

    @NotNull
    @Column(length = 50)
    @Length(message = "Este campo no puede estar vacío", min = 1, max = 50)
    private String name;


    @NotNull
    @JsonIgnore
    @Column(length = 250)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<TenantUser> tenants;
}
