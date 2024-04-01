package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.TransferCategory;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService transferService;

    private TransferController transferController;

    @BeforeEach
    void setUp() {
        transferController = new TransferController(transferService);
    }

    @Test
    void testCreate() {
        when(transferService.createTransfer(any(TransferRequestDto.class))).thenReturn(buildTransferResponseForTest());

        TransferRequestDto requestDto = new TransferRequestDto("12345678", TransferType.DEPOSIT,
                new BigDecimal("1500.47"), "reference", TransferCategory.TRAVEL);
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.create(requestDto);

        assertResponseEntity(transferResponseEntity, HttpStatus.CREATED);
        assertTransferResponse(transferResponseEntity.getBody());
    }

    @Test
    void testGetById() {
        when(transferService.getTransferById(any(UUID.class))).thenReturn(buildTransferResponseForTest());

        UUID id = UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226");
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.getById(id);

        assertResponseEntity(transferResponseEntity, HttpStatus.OK);
        assertTransferResponse(transferResponseEntity.getBody());
    }

    private void assertResponseEntity(ResponseEntity<TransferResponseDto> responseEntity, HttpStatus expectedStatus) {
        assertNotNull(responseEntity);
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    private void assertTransferResponse(TransferResponseDto transferResponseDto) {
        assertNotNull(transferResponseDto);
        assertEquals(UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226"), transferResponseDto.id());
        assertEquals(TransferType.DEPOSIT, transferResponseDto.type());
        assertEquals(new BigDecimal("1500.47"), transferResponseDto.amount());
        assertEquals("reference", transferResponseDto.reference());
        assertEquals(TransferCategory.TRAVEL, transferResponseDto.category());
        assertEquals(LocalDateTime.of(2024, Month.MARCH, 29, 20, 11, 3), transferResponseDto.timestamp());
    }

    private TransferResponseDto buildTransferResponseForTest() {
        UUID id = UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.MARCH, 29, 20, 11, 3);
        return new TransferResponseDto(id, TransferType.DEPOSIT, new BigDecimal("1500.47"),
                "reference", TransferCategory.TRAVEL, timestamp);
    }
}
