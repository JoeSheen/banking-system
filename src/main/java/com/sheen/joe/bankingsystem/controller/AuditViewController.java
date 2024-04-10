package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.service.AuditViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit-views")
public class AuditViewController {

    private final AuditViewService auditViewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AuditSummaryDto>> getAll(@RequestParam String entityId,
            @RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "commitDate") String sortProperty) {
        Page<AuditSummaryDto> auditSummaryPage = auditViewService
                .getAllForEntityId(pageNumber, pageSize, entityId, sortProperty);
        log.info("Page {} contains {} audit record(s)", auditSummaryPage.getNumber(), auditSummaryPage.getNumberOfElements());
        return new ResponseEntity<>(auditSummaryPage, HttpStatus.OK);
    }

    @GetMapping(path = "/{commitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditResponseDto> getById(@PathVariable("commitId") Long id) {
        AuditResponseDto auditResponseDto = auditViewService.getByCommitId(id);
        log.info("Audit with commit ID: {} found", auditResponseDto.commitId());
        return new ResponseEntity<>(auditResponseDto, HttpStatus.OK);
    }
}
