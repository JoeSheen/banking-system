package com.sheen.joe.bankingsystem.dto.audit;

import com.sheen.joe.bankingsystem.dto.CollectableDtoI;

import java.sql.Timestamp;

public record AuditSummaryDto(
        Long commitId,
        Timestamp commitDate,
        String entityType,
        String entityId
) implements CollectableDtoI {}
