package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
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
    public Page<AuditSummaryDto> getAllForEntityId(int pageNumber, int pageSize, String entityId, String sortProperty) {
        String authorUsername = SecurityUtils.getUsernameFromSecurityContext();
        Sort sort = Sort.by(new Order(Direction.ASC, sortProperty));
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        return auditViewRepository.findAllByEntityIdAndAuthor(entityId, authorUsername, paging)
                .map(auditViewMapper::toAuditSummary);
    }

    @Override
    public AuditResponseDto getByCommitId(Long commitId) {
        AuditView auditView = auditViewRepository.findByCommitId(commitId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Audit record with ID: %s not found", commitId)));
        if (!auditView.getAuthor().equals(SecurityUtils.getUsernameFromSecurityContext())) {
            throw new InvalidRequestException("Invalid Request");
        }
        return auditViewMapper.toAuditResponse(auditView);
    }

}