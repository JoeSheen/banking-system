package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.*;
import com.sheen.joe.bankingsystem.service.AuditViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<CollectionResponseDto<AuditSummaryDto>> getAll(@RequestParam String entityId,
            @RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "commitDate") String sortProperty) {
        CollectionResponseDto<AuditSummaryDto> responseDto = auditViewService
                .getAllForEntityId(pageNumber, pageSize, entityId, sortProperty);
        log.info("Page {} contains {} audit record(s)", responseDto.currentPage(), responseDto.content().size());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{commitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditResponseDto> getById(@PathVariable("commitId") Long id) {
        AuditResponseDto auditResponseDto = auditViewService.getByCommitId(id);
        log.info("Audit with commit ID: {} found", auditResponseDto.commitId());
        return new ResponseEntity<>(auditResponseDto, HttpStatus.OK);
    }
}
