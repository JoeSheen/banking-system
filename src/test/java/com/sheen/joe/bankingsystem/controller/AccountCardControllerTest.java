package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.service.AccountCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCardControllerTest {

    @Mock
    private AccountCardService accountCardService;

    private AccountCardController accountCardController;

    @BeforeEach
    void setUp() {
        accountCardController = new AccountCardController(accountCardService);
    }

    @Test
    void testGetById() {
        when(accountCardService.getAccountCardById(any(UUID.class)))
                .thenReturn(buildAccountCardResponseDtoForTest(true));

        UUID id = UUID.fromString("aa8e5895-df4e-47dd-b6fd-43aea39bc3da");
        ResponseEntity<AccountCardResponseDto> accountCardResponseEntity = accountCardController.getById(id);

        assertResponseEntity(accountCardResponseEntity);
        assertAccountCardResponseDto(accountCardResponseEntity.getBody(), true);
    }

    @Test
    void testDeactivate() {
        when(accountCardService.deactivateAccountCard(any(UUID.class)))
                .thenReturn(buildAccountCardResponseDtoForTest(false));

        UUID id = UUID.fromString("aa8e5895-df4e-47dd-b6fd-43aea39bc3da");
        ResponseEntity<AccountCardResponseDto> accountCardResponseEntity = accountCardController.deactivate(id);

        assertResponseEntity(accountCardResponseEntity);
        assertAccountCardResponseDto(accountCardResponseEntity.getBody(), false);
    }

    private void assertResponseEntity(ResponseEntity<AccountCardResponseDto> responseEntity) {
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.hasBody());
    }

    private void assertAccountCardResponseDto(AccountCardResponseDto responseDto, boolean expectedIsActive) {
        assertNotNull(responseDto);
        assertEquals(UUID.fromString("aa8e5895-df4e-47dd-b6fd-43aea39bc3da"), responseDto.id());
        assertEquals("1234 5678 1234 5678", responseDto.cardNumber());
        assertEquals("123", responseDto.cvc());
        assertEquals(expectedIsActive, responseDto.isActive());
        assertEquals(LocalDate.now(), responseDto.dateIssued());
        assertEquals(LocalDate.now().plusYears(4), responseDto.expirationDate());
        assertEquals("ANTHONY SLACK", responseDto.cardholderName());
    }

    private AccountCardResponseDto buildAccountCardResponseDtoForTest(boolean isActive) {
        UUID id = UUID.fromString("aa8e5895-df4e-47dd-b6fd-43aea39bc3da");
        return new AccountCardResponseDto(id, "1234 5678 1234 5678", "123", isActive,
                LocalDate.now(), LocalDate.now().plusYears(4), "ANTHONY SLACK");
    }
}
