package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.card.AccountCardSummaryDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferSummaryDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.mapper.impl.AccountMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapperImpl();
    }

    @Test
    void testToAccount() {
        Account account = accountMapper.toAccount(new AccountRequestDto("Test Account"));

        assertNotNull(account);
        assertEquals("Test Account", account.getAccountName());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertFalse(account.isClosed());
    }

    @Test
    void testToAccountResponse() {
        Account account = buildAccountForTest();
        AccountResponseDto accountResponseDto = accountMapper.toAccountResponse(account);

        assertNotNull(accountResponseDto);
        assertEquals(UUID.fromString("0e15ebf7-6d17-4901-bfd5-59d001a964b5"), accountResponseDto.id());
        assertEquals("Test Account", accountResponseDto.accountName());
        assertEquals("12345678", accountResponseDto.accountNumber());
        assertEquals(new AccountCardSummaryDto(UUID.fromString("a1a77c0c-ea30-4a6a-9de7-2ab77cf11d58"),
                "1234 5678 1234 5678"), accountResponseDto.card());
        assertEquals(BigDecimal.TEN, accountResponseDto.balance());
        assertEquals(List.of(expectedTransferSummaryDto()), accountResponseDto.transfers());
        assertEquals(LocalDateTime.of(2023, 1, 6, 9, 30, 0), accountResponseDto.createdAt());
        assertEquals(LocalDateTime.of(2024, 2, 21, 11, 45, 0), accountResponseDto.updatedAt());
    }

    private Account buildAccountForTest() {
        UUID id = UUID.fromString("0e15ebf7-6d17-4901-bfd5-59d001a964b5");
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 6, 9, 30, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 2, 21, 11, 45, 0);
        return Account.builder().id(id).accountName("Test Account").accountNumber("12345678")
                .accountCard(buildAccountCard()).balance(BigDecimal.TEN).createdAt(createdAt)
                .updatedAt(updatedAt).sentTransfers(List.of(buildTransferForTest())).build();
    }

    private Transfer buildTransferForTest() {
        UUID id = UUID.fromString("0fcefe52-edb0-4c48-a8fd-e2c4e447b32b");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.APRIL, 1, 16, 13, 59);
        return Transfer.builder().id(id).amount(new BigDecimal("15.99")).timestamp(timestamp).build();
    }

    private TransferSummaryDto expectedTransferSummaryDto() {
        UUID id = UUID.fromString("0fcefe52-edb0-4c48-a8fd-e2c4e447b32b");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.APRIL, 1, 16, 13, 59);
        return new TransferSummaryDto(id, new BigDecimal("15.99"), timestamp, '-');
    }

    private AccountCard buildAccountCard() {
        UUID id = UUID.fromString("a1a77c0c-ea30-4a6a-9de7-2ab77cf11d58");
        return AccountCard.builder().id(id).cardNumber("1234 5678 1234 5678").build();
    }
}
