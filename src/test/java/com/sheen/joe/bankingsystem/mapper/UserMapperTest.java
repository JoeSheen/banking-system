package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.account.AccountSummaryDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.mapper.impl.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private User user;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        user = buildUserForTest();
        userMapper = new UserMapperImpl();
    }

    @Test
    void testToUserResponse() {
        UserResponseDto responseDto = userMapper.toUserResponse(user);
        assertNotNull(responseDto);
        assertEquals(UUID.fromString("5d0d6908-40f6-4c3a-9390-2ddf40ae6437"), responseDto.id());
        assertEquals("James", responseDto.firstName());
        assertEquals("Tanner", responseDto.lastName());
        assertEquals(LocalDate.of(1974, Month.JULY, 25), responseDto.dateOfBirth());
        assertEquals("james.tanner@protonmail.com", responseDto.email());
        assertEquals("+44 1234 567890", responseDto.phoneNumber());
        assertEquals("JamesTanner5pMMcU", responseDto.username());
        assertEquals(Set.of(expectedAccountSummaryDto()), responseDto.accounts());
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("5d0d6908-40f6-4c3a-9390-2ddf40ae6437");
        LocalDate dateOfBirth = LocalDate.of(1974, Month.JULY, 25);
        return User.builder().id(id).firstName("James").lastName("Tanner").dateOfBirth(dateOfBirth)
                .email("james.tanner@protonmail.com").phoneNumber("+44 1234 567890")
                .username("JamesTanner5pMMcU").accounts(Set.of(buildAccountForTest())).build();
    }

    private Account buildAccountForTest() {
        UUID id = UUID.fromString("f94d4996-9ff7-4b2b-83e1-5d64230c612e");
        LocalDateTime updatedAt = LocalDateTime.of(2024, Month.APRIL, 1, 16, 9, 50);
        return Account.builder().id(id).accountName("Test Account")
                .balance(new BigDecimal("1500")).updatedAt(updatedAt).build();
    }

    private AccountSummaryDto expectedAccountSummaryDto() {
        UUID id = UUID.fromString("f94d4996-9ff7-4b2b-83e1-5d64230c612e");
        LocalDateTime updatedAt = LocalDateTime.of(2024, Month.APRIL, 1, 16, 9, 50);
        return new AccountSummaryDto(id, "Test Account", new BigDecimal("1500"), updatedAt);
    }
}
