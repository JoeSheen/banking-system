package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditResponseDto;
import com.sheen.joe.bankingsystem.dto.audit.AuditSummaryDto;
import com.sheen.joe.bankingsystem.entity.AuditView;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AuditViewMapper;
import com.sheen.joe.bankingsystem.mapper.impl.AuditViewMapperImpl;
import com.sheen.joe.bankingsystem.repository.AuditViewRepository;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.AuditViewServiceImpl;
import org.javers.core.metamodel.object.SnapshotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditViewServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AuditViewRepository auditViewRepository;

    private AuditViewService auditViewService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        AuditViewMapper auditViewMapper = new AuditViewMapperImpl();
        auditViewService = new AuditViewServiceImpl(auditViewRepository, auditViewMapper);
    }

    @Test
    void testGetAllForEntityId() {
        Page<AuditView> auditViewPage = new PageImpl<>(List.of(buildAuditViewForTest()));
        when(auditViewRepository.findAllByEntityIdAndAuthor(anyString(), anyString(), any())).thenReturn(auditViewPage);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest("DavidGriffin6uodCE"));

        String entityId = "5a0346d2-74f9-4ce2-afd6-19ec16d4be67";
        CollectionResponseDto<AuditSummaryDto> collectionResponseDto = auditViewService
                .getAllForEntityId(0, 50, entityId, "commitDate");

        // assert collection
        assertEquals(0, collectionResponseDto.currentPage());
        assertEquals(1, collectionResponseDto.totalPages());
        assertEquals(1, collectionResponseDto.totalElements());
        assertFalse(collectionResponseDto.content().isEmpty());
        // assert content of AuditSummaryDto
        AuditSummaryDto auditSummaryDto = collectionResponseDto.content().get(0);
        assertNotNull(auditSummaryDto);
        assertEquals(1L, auditSummaryDto.commitId());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 17, 35, 38)), auditSummaryDto.commitDate());
        assertEquals("Account", auditSummaryDto.entityType());
        assertEquals("5a0346d2-74f9-4ce2-afd6-19ec16d4be67", auditSummaryDto.entityId());
    }

    @Test
    void testGetByCommitId() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest("DavidGriffin6uodCE"));
        when(auditViewRepository.findByCommitIdAndAuthor(any(), anyString())).thenReturn(Optional.of(buildAuditViewForTest()));

        AuditResponseDto auditResponseDto = auditViewService.getByCommitId(1L);

        assertNotNull(auditResponseDto);
        assertEquals(1L, auditResponseDto.commitId());
        assertEquals("David", auditResponseDto.firstName());
        assertEquals("Griffin", auditResponseDto.lastName());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 17, 35, 38)), auditResponseDto.commitDate());
        assertEquals("Account", auditResponseDto.entityType());
        assertEquals("5a0346d2-74f9-4ce2-afd6-19ec16d4be67", auditResponseDto.entityId());
        assertEquals(SnapshotType.UPDATE, auditResponseDto.auditType());
        assertEquals("[balance]", auditResponseDto.changedProperties());
        assertEquals("{\"balance\": 250.60}", auditResponseDto.auditState());
    }

    @Test
    void testGetByCommitIdThrowsResourceNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest("JessicaBrown4x9Gqs"));
        when(auditViewRepository.findByCommitIdAndAuthor(any(), anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                auditViewService.getByCommitId(1L));

        String expectedMessage = "Audit record with ID: 1 for user not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private SecurityUser buildSecurityUserForTest(String username) {
        return new SecurityUser(UUID.randomUUID(), Set.of(UserRole.USER_ROLE), "password", username);
    }

    private AuditView buildAuditViewForTest() {
        Timestamp commitDate = Timestamp.valueOf(LocalDateTime.of(2024, Month.APRIL, 7, 17, 35, 38));
        return new AuditView(1L, "DavidGriffin6uodCE", "David", "Griffin",
                commitDate, "Account", "5a0346d2-74f9-4ce2-afd6-19ec16d4be67",
                SnapshotType.UPDATE, "[balance]", "{\"balance\": 250.60}");
    }
}
