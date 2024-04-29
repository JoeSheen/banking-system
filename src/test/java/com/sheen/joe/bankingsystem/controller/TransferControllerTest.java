package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
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
    void testPerformDeposit() {
        when(transferService.createTransfer(any(DepositWithdrawTransferRequestDto.class)))
                .thenReturn(buildTransferResponseForTest(TransferType.DEPOSIT, "Deposit", TransferCategory.GENERAL, new BigDecimal("15.30"), '+'));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.DEPOSIT, new BigDecimal("25.99"));
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.performDeposit(transferRequestDto);

        assertResponseEntity(transferResponseEntity, HttpStatus.CREATED);
        assertTransferResponse(transferResponseEntity.getBody(), TransferType.DEPOSIT, new BigDecimal("15.30"),
                "Deposit", TransferCategory.GENERAL, '+');
    }

    @Test
    void testPerformWithdrawal() {
        when(transferService.createTransfer(any(DepositWithdrawTransferRequestDto.class)))
                .thenReturn(buildTransferResponseForTest(TransferType.WITHDRAW, "Withdraw", TransferCategory.GENERAL, new BigDecimal("15.30"), '-'));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.WITHDRAW, new BigDecimal("15.30"));
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.performWithdrawal(transferRequestDto);

        assertResponseEntity(transferResponseEntity, HttpStatus.CREATED);
        assertTransferResponse(transferResponseEntity.getBody(), TransferType.WITHDRAW, new BigDecimal("15.30"),
                "Withdraw", TransferCategory.GENERAL, '-');
    }

    @Test
    void testPerformTransfer() {
        when(transferService.createTransfer(any(TransferRequestDto.class)))
                .thenReturn(buildTransferResponseForTest(TransferType.PERSONAL, "reference", TransferCategory.TRAVEL, new BigDecimal("75.00"), '+'));

        TransferRequestDto transferRequestDto = new TransferRequestDto("12345678", "12-34-56",
                "87654321", "65-43-21", "Jeffrey Oshea",
                TransferType.PERSONAL, TransferCategory.TRAVEL, "reference", new BigDecimal("75.00"));
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.performTransfer(transferRequestDto);

        assertResponseEntity(transferResponseEntity, HttpStatus.CREATED);
        assertTransferResponse(transferResponseEntity.getBody(), TransferType.PERSONAL, new BigDecimal("75.00"),
                "reference", TransferCategory.TRAVEL, '+');
    }

    @Test
    void testGetById() {
        when(transferService.getTransferById(any(UUID.class)))
                .thenReturn(buildTransferResponseForTest(TransferType.DEPOSIT, "Deposit", TransferCategory.GENERAL, new BigDecimal("150.47"), '-'));

        UUID id = UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226");
        ResponseEntity<TransferResponseDto> transferResponseEntity = transferController.getById(id);

        assertResponseEntity(transferResponseEntity, HttpStatus.OK);
        assertTransferResponse(transferResponseEntity.getBody(), TransferType.DEPOSIT, new BigDecimal("150.47"),
                "Deposit", TransferCategory.GENERAL, '-');
    }

    private void assertResponseEntity(ResponseEntity<TransferResponseDto> responseEntity, HttpStatus expectedStatus) {
        assertNotNull(responseEntity);
        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    private void assertTransferResponse(TransferResponseDto transferResponseDto, TransferType expectedType,
            BigDecimal expectedAmount, String expectedReference, TransferCategory expectedCategory, char expectedSymbol) {
        assertNotNull(transferResponseDto);
        assertEquals(UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226"), transferResponseDto.id());
        assertEquals(expectedType, transferResponseDto.type());
        assertEquals(expectedAmount, transferResponseDto.amount());
        assertEquals(expectedReference, transferResponseDto.reference());
        assertEquals(expectedCategory, transferResponseDto.category());
        assertEquals(LocalDateTime.of(2024, Month.MARCH, 29, 20, 11, 3), transferResponseDto.timestamp());
        assertEquals(expectedSymbol, transferResponseDto.symbol());
    }

    private TransferResponseDto buildTransferResponseForTest(TransferType type, String reference, TransferCategory category,
            BigDecimal amount, char symbol) {
        UUID id = UUID.fromString("ff9efba8-7953-4065-8499-5f86cb3bc226");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.MARCH, 29, 20, 11, 3);
        return new TransferResponseDto(id, type, amount, reference, category, timestamp, symbol);
    }
}
