package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.service.AuditViewService;
import org.javers.core.metamodel.object.SnapshotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditViewControllerTest {

    @Mock
    private AuditViewService auditViewService;

    private AuditViewController auditViewController;

    @BeforeEach
    void setUp() {
        auditViewController = new AuditViewController(auditViewService);
    }

    @Test
    void testGetAll() {
        List<AuditSummaryDto> content = List.of(buildAuditSummaryDtoForTest());
        when(auditViewService.getAllForEntityId(0, 50, "commitDate", "commitDate"))
                .thenReturn(new PageImpl<>(content));

        ResponseEntity<Page<AuditSummaryDto>> auditSummaryPageResponseEntity =
                auditViewController.getAll("commitDate",0, 50, "commitDate");

        // assert response entity
        assertNotNull(auditSummaryPageResponseEntity);
        assertEquals(auditSummaryPageResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(auditSummaryPageResponseEntity.hasBody());
        // assert page
        Page<AuditSummaryDto> auditSummaryPage = auditSummaryPageResponseEntity.getBody();
        assertNotNull(auditSummaryPage);
        assertEquals(0, auditSummaryPage.getNumber());
        assertEquals(1, auditSummaryPage.getNumberOfElements());
        // assert account response
        AuditSummaryDto auditSummaryDto = auditSummaryPage.getContent().get(0);
        assertNotNull(auditSummaryDto);
        assertEquals(1L, auditSummaryDto.commitId());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL,7, 15,6,39)), auditSummaryDto.commitDate());
        assertEquals("Account", auditSummaryDto.entityType());
        assertEquals("8957d238-c0b6-44d1-8170-1fe6739b78fa", auditSummaryDto.entityId());
    }

    @Test
    void testGetById() {
        when(auditViewService.getByCommitId(any(Long.class))).thenReturn(buildAuditResponseDtoForTest());

        Long commitId = 1L;
        ResponseEntity<AuditResponseDto> auditResponseEntity = auditViewController.getById(commitId);

        // assert response entity
        assertNotNull(auditResponseEntity);
        assertEquals(HttpStatus.OK, auditResponseEntity.getStatusCode());
        assertTrue(auditResponseEntity.hasBody());
        // assert response content
        AuditResponseDto auditResponseDto = auditResponseEntity.getBody();
        assertNotNull(auditResponseDto);
        assertEquals(1L, auditResponseDto.commitId());
        assertEquals("Jeanette", auditResponseDto.firstName());
        assertEquals("Woodruff", auditResponseDto.lastName());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL,7, 15,6,39)), auditResponseDto.commitDate());
        assertEquals("Account", auditResponseDto.entityType());
        assertEquals("8957d238-c0b6-44d1-8170-1fe6739b78fa", auditResponseDto.entityId());
        assertEquals(SnapshotType.UPDATE, auditResponseDto.auditType());
        assertEquals("[some changed property]", auditResponseDto.changedProperties());
        assertEquals("some changed audit state", auditResponseDto.auditState());
    }

    private AuditResponseDto buildAuditResponseDtoForTest() {
        Timestamp commitDate = Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL,7, 15,6,39));
        return new AuditResponseDto(1L,"Jeanette", "Woodruff", commitDate,
                "Account", "8957d238-c0b6-44d1-8170-1fe6739b78fa", SnapshotType.UPDATE,
                "[some changed property]", "some changed audit state");
    }

    private AuditSummaryDto buildAuditSummaryDtoForTest() {
        Timestamp commitDate = Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL,7, 15,6,39));
        return new AuditSummaryDto(1L, commitDate, "Account", "8957d238-c0b6-44d1-8170-1fe6739b78fa");
    }

}
