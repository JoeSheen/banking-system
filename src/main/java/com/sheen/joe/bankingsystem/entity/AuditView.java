package com.sheen.joe.bankingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.javers.core.metamodel.object.SnapshotType;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Subselect("SELECT * FROM audit_view")
public class AuditView {

    @Id
    private Long commitId;

    private String author;

    private String firstName;

    private String lastName;

    private Timestamp commitDate;

    private String entityType;

    private String entityId;

    @Enumerated(EnumType.STRING)
    private SnapshotType auditType;

    private String changedProperties;

    private String auditState;
}
