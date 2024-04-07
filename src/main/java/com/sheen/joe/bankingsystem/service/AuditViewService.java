package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.AuditSummaryDto;
import org.springframework.data.domain.Page;

public interface AuditViewService {

    Page<AuditSummaryDto> getAllForEntityId(int pageNumber, int pageSize, String entityId, String sortProperty);

    AuditResponseDto getByCommitId(Long commitId);
}
