package com.sheen.joe.bankingsystem.dto;

import org.javers.core.metamodel.object.SnapshotType;

import java.sql.Timestamp;

public record AuditResponseDto(
        Long commitId,
        String firstName,
        String lastName,
        Timestamp commitDate,
        String entityType,
        String entityId,
        SnapshotType auditType,
        String changedProperties,
        String auditState
) {}
