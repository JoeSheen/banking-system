package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.card.AccountCardSummaryDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferSummaryDto;
import com.sheen.joe.bankingsystem.service.AccountService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountController = new AccountController(accountService);
    }

    @Test
    void testCreate() {
        when(accountService.createAccount(any(AccountRequestDto.class)))
                .thenReturn(buildAccountResponseDtoForTest("Account Name"));

        AccountRequestDto accountRequestDto = new AccountRequestDto("Account Name");
        ResponseEntity<AccountResponseDto> accountResponseEntity = accountController.create(accountRequestDto);

        assertResponseEntity(accountResponseEntity, HttpStatus.CREATED);
        assertAccountResponseDto(accountResponseEntity.getBody(), "Account Name");
    }

    @Test
    void testUpdate() {
        when(accountService.updateAccount(any(UUID.class), any(AccountRequestDto.class)))
                .thenReturn(buildAccountResponseDtoForTest("Updated Account Name"));

        UUID id = UUID.randomUUID();
        AccountRequestDto accountRequestDto = new AccountRequestDto("Updated Account Name");
        ResponseEntity<AccountResponseDto> accountResponseEntity = accountController.update(id, accountRequestDto);

        assertResponseEntity(accountResponseEntity, HttpStatus.OK);
        assertAccountResponseDto(accountResponseEntity.getBody(), "Updated Account Name");
    }

    @Test
    void testGetAll() {
        List<AccountResponseDto> content = List.of(buildAccountResponseDtoForTest("Account Name"));
        when(accountService.getAllUserAccounts(0, 5, false, "updatedAt"))
                .thenReturn(new PageImpl<>(content));

        ResponseEntity<Page<AccountResponseDto>> accountPageResponseEntity =
                accountController.getAll(0, 5, false, "updatedAt");

        // assert response entity
        assertNotNull(accountPageResponseEntity);
        assertEquals(accountPageResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(accountPageResponseEntity.hasBody());
        // assert page
        assertNotNull(accountPageResponseEntity.getBody());
        assertEquals(0, accountPageResponseEntity.getBody().getNumber());
        assertEquals(1, accountPageResponseEntity.getBody().getNumberOfElements());
        // assert account response
        assertAccountResponseDto(accountPageResponseEntity.getBody().getContent().get(0), "Account Name");
    }

    @Test
    void testGetById() {
        when(accountService.getAccountById(any(UUID.class)))
                .thenReturn(buildAccountResponseDtoForTest("Account Name"));

        UUID id = UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12");
        ResponseEntity<AccountResponseDto> accountResponseEntity = accountController.getById(id);

        assertResponseEntity(accountResponseEntity, HttpStatus.OK);
        assertAccountResponseDto(accountResponseEntity.getBody(), "Account Name");
    }

    @Test
    void testClose() {
        UUID id = UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12");
        String message = String.format("Account with ID: %s successfully closed", id);
        when(accountService.closeAccount(any(UUID.class))).thenReturn(Pair.of(true, message));

        ResponseEntity<String> closedResponseEntity = accountController.close(id);

        // assert response entity
        assertNotNull(closedResponseEntity);
        assertEquals(closedResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(closedResponseEntity.hasBody());
        // assert response content
        assertNotNull(closedResponseEntity.getBody());
        assertEquals(String.format("Account with ID: %s successfully closed", id), closedResponseEntity.getBody());

    }

    @Test
    void testCloseReturnsBadRequest() {
        String message = "Account balance must be 0.00";
        when(accountService.closeAccount(any(UUID.class))).thenReturn(Pair.of(false, message));

        UUID id = UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12");
        ResponseEntity<String> closedResponseEntity = accountController.close(id);

        // assert response entity
        assertNotNull(closedResponseEntity);
        assertEquals(closedResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(closedResponseEntity.hasBody());
        // assert response content
        assertNotNull(closedResponseEntity.getBody());
        assertEquals("Account balance must be 0.00", closedResponseEntity.getBody());
    }

    @Test
    void testNewCardForAccount() {
        when(accountService.requestNewCardForAccount(any(UUID.class)))
                .thenReturn(buildAccountResponseDtoForTest("Account Name"));

        UUID id = UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12");
        ResponseEntity<AccountResponseDto> accountResponseEntity = accountController.newCardForAccount(id);

        assertResponseEntity(accountResponseEntity, HttpStatus.OK);
        assertAccountResponseDto(accountResponseEntity.getBody(), "Account Name");
    }

    private void assertResponseEntity(ResponseEntity<AccountResponseDto> responseEntity, HttpStatus status) {
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), status);
        assertTrue(responseEntity.hasBody());
    }

    private void assertAccountResponseDto(AccountResponseDto accountResponseDto, String expectedName) {
        assertNotNull(accountResponseDto);
        assertEquals(UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12"), accountResponseDto.id());
        assertEquals(expectedName, accountResponseDto.accountName());
        assertEquals("12345678", accountResponseDto.accountNumber());
        assertEquals(new AccountCardSummaryDto(UUID.fromString("b706649c-630e-48f0-b1e3-5cb35caafda5"),
                        "1234 5678 1234 5678"), accountResponseDto.card());
        assertEquals(BigDecimal.TEN, accountResponseDto.balance());
        assertEquals(List.of(buildTransferSummaryDtoForTest()), accountResponseDto.transfers());
        assertEquals(LocalDateTime.of(2023, 4, 6, 12, 30, 0), accountResponseDto.createdAt());
        assertEquals(LocalDateTime.of(2024, 5, 11, 18, 45, 0), accountResponseDto.updatedAt());
    }

    private AccountResponseDto buildAccountResponseDtoForTest(String accountName) {
        UUID id = UUID.fromString("7a46dedc-491e-4567-9024-499c62ba5f12");
        LocalDateTime createdAt = LocalDateTime.of(2023, 4, 6, 12, 30, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 5, 11, 18, 45, 0);
        AccountCardSummaryDto card = new AccountCardSummaryDto(UUID.fromString("b706649c-630e-48f0-b1e3-5cb35caafda5"),
                "1234 5678 1234 5678");

        return new AccountResponseDto(id, accountName, "12345678", card,
                BigDecimal.TEN, List.of(buildTransferSummaryDtoForTest()), createdAt, updatedAt);
    }

    private TransferSummaryDto buildTransferSummaryDtoForTest() {
        UUID id = UUID.fromString("2e7f9f62-ef5e-499c-bce6-565999315c22");
        LocalDateTime now = LocalDateTime.of(2024, Month.APRIL, 1,15,38,50);
        return new TransferSummaryDto(id, new BigDecimal("5.99"), now);
    }
}
