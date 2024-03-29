package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.mapper.impl.TransferMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransferMapperTest {

    private TransferMapper transferMapper;

    @BeforeEach
    void setUp() {
        transferMapper = new TransferMapperImpl();
    }

    @Test
    void testToTransfer() {
        TransferRequestDto requestDto = new TransferRequestDto("12345678", TransferType.DEPOSIT,
                new BigDecimal("100.99"), "Transfer Reference");

        Transfer transfer = transferMapper.toTransfer(requestDto);

        assertNotNull(transfer);
        assertEquals(TransferType.DEPOSIT, transfer.getTransferType());
        assertEquals(new BigDecimal("100.99"),transfer.getAmount());
        assertEquals("Transfer Reference", transfer.getReference());
    }

    @Test
    void testToTransferResponse() {
        UUID id = UUID.fromString("02fa9f78-e259-4bbe-8964-b8ea4937373c");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.MARCH, 29, 14, 30, 0);
        Transfer transfer = Transfer.builder().id(id).amount(new BigDecimal("100.99")).timestamp(timestamp).build();

        TransferResponseDto transferResponseDto = transferMapper.toTransferResponse(transfer);

        assertNotNull(transferResponseDto);
        assertEquals(UUID.fromString("02fa9f78-e259-4bbe-8964-b8ea4937373c"), transferResponseDto.id());
        assertEquals(new BigDecimal("100.99"), transferResponseDto.amount());
        assertEquals(LocalDateTime.of(2024, Month.MARCH, 29, 14, 30, 0), transferResponseDto.timestamp());
    }
}
