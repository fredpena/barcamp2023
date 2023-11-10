package dev.fredpena.barcamp.data.tenant.entity;

import dev.fredpena.barcamp.security.auditable.RevisionListenerImpl;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Getter
@Setter
@Entity
@RevisionEntity(RevisionListenerImpl.class)
public class Revision extends DefaultRevisionEntity {

    protected String modifierUser;
    protected String ipAddress;

}
