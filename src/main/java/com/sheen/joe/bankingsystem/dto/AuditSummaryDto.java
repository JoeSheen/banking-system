package com.sheen.joe.bankingsystem.dto;

import java.sql.Timestamp;

public record AuditSummaryDto(
        Long commitId,
        Timestamp commitDate,
        String entityType,
        String entityId
) {}
