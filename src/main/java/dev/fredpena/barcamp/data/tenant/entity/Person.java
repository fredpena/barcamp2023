package dev.fredpena.barcamp.data.tenant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@ToString
@Getter
@Setter
@Entity
@Audited(withModifiedFlag = true)
@EqualsAndHashCode(of = "code", callSuper = false)
@Table(name = "person", indexes = {@Index(columnList = "firstName"), @Index(columnList = "lastName"), @Index(columnList = "email")})
public class Person extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    @NotNull
    @Column(length = 50)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String firstName;
    @NotNull
    @Column(length = 50)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String lastName;
    @Email
    @NotNull
    @Column(length = 100)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String email;
    @NotNull
    @Column(length = 50)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String phone;
    @NotNull(message = "This field can not be blank.")
    private LocalDate dateOfBirth;
    @NotNull
    @Column(length = 100)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String occupation;
    @NotNull
    @Column(length = 50)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String role;

    private boolean important;


}
