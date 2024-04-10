package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;
import com.sheen.joe.bankingsystem.mapper.impl.AuditViewMapperImpl;
import org.javers.core.metamodel.object.SnapshotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class AuditViewMapperTest {

    private AuditViewMapper auditViewMapper;

    private AuditView auditView;

    @BeforeEach
    void setUp() {
        auditView = buildAuditViewForTest();
        auditViewMapper = new AuditViewMapperImpl();
    }

    @Test
    void testToAuditResponse() {
        AuditResponseDto auditResponseDto = auditViewMapper.toAuditResponse(auditView);

        assertNotNull(auditResponseDto);
        assertEquals(1L, auditResponseDto.commitId());
        assertEquals("Timothy", auditResponseDto.firstName());
        assertEquals("Kelley", auditResponseDto.lastName());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 7, 39, 14)), auditResponseDto.commitDate());
        assertEquals("User", auditResponseDto.entityType());
        assertEquals("cab792e3-167f-4027-b64c-e67e9e87df27", auditResponseDto.entityId());
        assertEquals(SnapshotType.INITIAL, auditResponseDto.auditType());
        assertEquals("some changed properties", auditResponseDto.changedProperties());
        assertEquals("some audit state", auditResponseDto.auditState());
    }

    @Test
    void testToAuditSummary() {
        AuditSummaryDto auditSummaryDto = auditViewMapper.toAuditSummary(auditView);

        assertNotNull(auditSummaryDto);
        assertEquals(1L, auditSummaryDto.commitId());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 7, 39, 14)), auditSummaryDto.commitDate());
        assertEquals("User", auditSummaryDto.entityType());
        assertEquals("cab792e3-167f-4027-b64c-e67e9e87df27", auditSummaryDto.entityId());
    }

    private AuditView buildAuditViewForTest() {
        Timestamp commitDate = Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 7, 39, 14));

        return new AuditView(1L, "TimothyKelley3CfhTg", "Timothy", "Kelley",
                commitDate, "User", "cab792e3-167f-4027-b64c-e67e9e87df27", SnapshotType.INITIAL,
                "some changed properties", "some audit state");
    }
}