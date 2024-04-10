package com.sheen.joe.bankingsystem.dto.audit;

import java.sql.Timestamp;

public record AuditSummaryDto(
        Long commitId,
        Timestamp commitDate,
        String entityType,
        String entityId
) {}
