package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;

public interface AuditViewMapper {

    AuditResponseDto toAuditResponse(AuditView auditView);

    AuditSummaryDto toAuditSummary(AuditView auditView);

}
