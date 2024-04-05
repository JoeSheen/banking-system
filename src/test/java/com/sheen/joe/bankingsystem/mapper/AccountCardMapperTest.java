package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.mapper.impl.AccountCardMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountCardMapperTest {

    private AccountCardMapper accountCardMapper;

    private AccountCard card;

    @BeforeEach
    void setUp() {
        card = buildAccountCardForTest();

        accountCardMapper = new AccountCardMapperImpl();
    }

    @Test
    void testToAccountCardResponse() {
        AccountCardResponseDto cardResponseDto = accountCardMapper.toAccountCardResponse(card);

        assertNotNull(cardResponseDto);
        assertEquals(UUID.fromString("45bc6bd4-151c-4639-9380-14afb10fc9f1"), cardResponseDto.id());
        assertEquals("1234 5678 1234 5678", cardResponseDto.cardNumber());
        assertEquals("123", cardResponseDto.cvc());
        assertTrue(cardResponseDto.isActive());
        assertEquals(LocalDate.now(), cardResponseDto.dateIssued());
        assertEquals(LocalDate.now().plusYears(4), cardResponseDto.expirationDate());
        assertEquals("LARA KENDRICKS", cardResponseDto.cardholderName());
    }

    private AccountCard buildAccountCardForTest() {
        UUID id = UUID.fromString("45bc6bd4-151c-4639-9380-14afb10fc9f1");
        LocalDate dateIssued = LocalDate.now();
        LocalDate expirationDate = dateIssued.plusYears(4);
        return AccountCard.builder().id(id).cardNumber("1234 5678 1234 5678").cvc("123")
                .isActive(true).dateIssued(dateIssued).expirationDate(expirationDate)
                .cardholderName("LARA KENDRICKS").build();
    }
}
