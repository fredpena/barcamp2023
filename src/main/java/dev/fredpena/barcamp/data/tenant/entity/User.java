package dev.fredpena.barcamp.data.tenant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.Instant;

@ToString
@Getter
@Setter
@Entity
@Audited( withModifiedFlag = true )
@EqualsAndHashCode(of = "userId", callSuper = false)
@Table(name = "userr", indexes = {@Index(columnList = "username"), @Index(columnList = "name"), @Index(columnList = "email")})
public class User extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(length = 30, unique = true, updatable = false)
    @Length(message = "This field can not be blank.", min = 1, max = 30)
    private String username;

    @NotNull
    @JsonIgnore
    @Column(length = 250)
    private String password;

    @NotNull
    @Column(length = 50)
    @Length(message = "This field can not be blank.", min = 1, max = 50)
    private String name;

    @Column(length = 50)
    private String email;

    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    private boolean locked;
    private boolean deleted;
    private boolean oneLogPwd;

    @NotAudited
    private Instant lastLoginTs;

}
