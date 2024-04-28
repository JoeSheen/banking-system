package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferCategory;
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

    private final UUID accountId = UUID.fromString("6042d86c-0a8c-47a7-b80e-6064b801c3fe");

    @BeforeEach
    void setUp() {
        transferMapper = new TransferMapperImpl();
    }

    @Test
    void testToTransferForDepositWithdrawTransferRequestDto() {
        DepositWithdrawTransferRequestDto requestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.DEPOSIT, new BigDecimal("100.99"));

        Transfer transfer = transferMapper.toTransfer(requestDto, buildAccountForTest("12345678", "12-34-56", accountId));

        assertNotNull(transfer);
        assertEquals(TransferType.DEPOSIT, transfer.getTransferType());
        assertEquals(new BigDecimal("100.99"), transfer.getAmount());
        assertEquals("Deposit", transfer.getReference());
        assertEquals(TransferCategory.GENERAL, transfer.getCategory());
        assertEquals(UUID.fromString("6042d86c-0a8c-47a7-b80e-6064b801c3fe"), transfer.getSenderAccount().getId());
    }

    @Test
    void testToTransferForTransferRequestDto() {
        UUID receiverAccountId = UUID.randomUUID();
        TransferRequestDto requestDto = new TransferRequestDto("910111213", "78-91-11",
                "87654321", "65-43-21", "Joe Adams",
                TransferType.PERSONAL, TransferCategory.ENTERTAINMENT, "reference", new BigDecimal("250.50"));

        Account senderAccount = buildAccountForTest("910111213", "78-91-11", accountId);
        Account receiverAccount = buildAccountForTest("87654321", "65-43-21", receiverAccountId);
        Transfer transfer = transferMapper.toTransfer(requestDto, senderAccount, receiverAccount);

        assertNotNull(transfer);
        assertEquals(TransferType.PERSONAL, transfer.getTransferType());
        assertEquals(new BigDecimal("250.50"), transfer.getAmount());
        assertEquals("reference", transfer.getReference());
        assertEquals(TransferCategory.ENTERTAINMENT, transfer.getCategory());
        assertEquals(UUID.fromString("6042d86c-0a8c-47a7-b80e-6064b801c3fe"), transfer.getSenderAccount().getId());
        assertEquals(receiverAccountId, transfer.getReceiverAccount().getId());
    }

    @Test
    void testToTransferResponse() {
        UUID id = UUID.fromString("02fa9f78-e259-4bbe-8964-b8ea4937373c");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.MARCH, 29, 14, 30, 0);
        Transfer transfer = Transfer.builder().id(id).transferType(TransferType.WITHDRAW).amount(new BigDecimal("100.99"))
                .reference("reference").category(TransferCategory.ENTERTAINMENT).timestamp(timestamp).build();

        TransferResponseDto transferResponseDto = transferMapper.toTransferResponse(transfer);

        assertNotNull(transferResponseDto);
        assertEquals(UUID.fromString("02fa9f78-e259-4bbe-8964-b8ea4937373c"), transferResponseDto.id());
        assertEquals(TransferType.WITHDRAW, transferResponseDto.type());
        assertEquals(new BigDecimal("100.99"), transferResponseDto.amount());
        assertEquals("reference", transferResponseDto.reference());
        assertEquals(TransferCategory.ENTERTAINMENT, transferResponseDto.category());
        assertEquals(LocalDateTime.of(2024, Month.MARCH, 29, 14, 30, 0), transferResponseDto.timestamp());
    }

    private Account buildAccountForTest(String accountNumber, String sortCode, UUID id) {
        return Account.builder().id(id).accountNumber(accountNumber).sortCode(sortCode).build();
    }
}
