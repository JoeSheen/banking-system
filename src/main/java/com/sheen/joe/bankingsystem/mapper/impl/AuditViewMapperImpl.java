package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;
import com.sheen.joe.bankingsystem.mapper.AuditViewMapper;
import org.springframework.stereotype.Component;

@Component
public class AuditViewMapperImpl implements AuditViewMapper {

    @Override
    public AuditResponseDto toAuditResponse(AuditView auditView) {
        return new AuditResponseDto(auditView.getCommitId(), auditView.getFirstName(), auditView.getLastName(),
                auditView.getCommitDate(), auditView.getEntityType(), auditView.getEntityId(),
                auditView.getAuditType(), auditView.getChangedProperties(), auditView.getAuditState());
    }

    @Override
    public AuditSummaryDto toAuditSummary(AuditView auditView) {
        return new AuditSummaryDto(auditView.getCommitId(), auditView.getCommitDate(),
                auditView.getEntityType(), auditView.getEntityId());
    }

}
