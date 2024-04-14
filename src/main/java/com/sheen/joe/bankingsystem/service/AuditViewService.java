package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;

public interface AuditViewService {

    CollectionResponseDto<AuditSummaryDto> getAllForEntityId(int pageNumber, int pageSize, String entityId, String sortProperty);

    AuditResponseDto getByCommitId(Long commitId);
}
