package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.mapper.impl.AccountMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        assertEquals("1234 5678 1234 5678", accountResponseDto.cardNumber());
        assertEquals("123", accountResponseDto.cvc());
        assertEquals(BigDecimal.TEN, accountResponseDto.balance());
        assertEquals(LocalDateTime.of(2023, 1, 6, 9, 30, 0), accountResponseDto.createdAt());
        assertEquals(LocalDateTime.of(2024, 2, 21, 11, 45, 0), accountResponseDto.updatedAt());
    }

    private Account buildAccountForTest() {
        UUID id = UUID.fromString("0e15ebf7-6d17-4901-bfd5-59d001a964b5");
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 6, 9, 30, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 2, 21, 11, 45, 0);
        return Account.builder().id(id).accountName("Test Account").accountNumber("12345678")
                .cardNumber("1234 5678 1234 5678").cvc("123").balance(BigDecimal.TEN)
                .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
