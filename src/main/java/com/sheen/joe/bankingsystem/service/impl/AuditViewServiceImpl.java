package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AuditViewMapper;
import com.sheen.joe.bankingsystem.repository.AuditViewRepository;
import com.sheen.joe.bankingsystem.security.SecurityUtils;
import com.sheen.joe.bankingsystem.service.AuditViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditViewServiceImpl implements AuditViewService {

    private final AuditViewRepository auditViewRepository;

    private final AuditViewMapper auditViewMapper;

    @Override
    public CollectionResponseDto<AuditSummaryDto> getAllForEntityId(int pageNumber, int pageSize, String entityId, String sortProperty) {
        String author = SecurityUtils.getUserIdFromSecurityContext().toString();
        Sort sort = Sort.by(new Order(Direction.ASC, sortProperty));
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);

        Page<AuditSummaryDto> page = auditViewRepository.findAllByEntityIdAndAuthor(entityId, author, paging)
                .map(auditViewMapper::toAuditSummary);

        return new CollectionResponseDto<>(page.getContent(), page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSort().isSorted());
    }

    @Override
    public AuditResponseDto getByCommitId(Long commitId) {
        String author = SecurityUtils.getUserIdFromSecurityContext().toString();
        AuditView auditView = auditViewRepository.findByCommitIdAndAuthor(commitId, author).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Audit record with ID: %s for user not found", commitId)));
        return auditViewMapper.toAuditResponse(auditView);
    }

}
