package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AccountCardMapper;
import com.sheen.joe.bankingsystem.mapper.impl.AccountCardMapperImpl;
import com.sheen.joe.bankingsystem.repository.AccountCardRepository;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.AccountCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCardServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AccountCardRepository accountCardRepository;

    private AccountCardService accountCardService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        AccountCardMapper accountMapper = new AccountCardMapperImpl();

        accountCardService = new AccountCardServiceImpl(accountCardRepository, accountMapper);
    }

    @Test
    void testGetAccountCardById() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(accountCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountCardForTest(true)));

        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");
        AccountCardResponseDto responseDto = accountCardService.getAccountCardById(id);
        assertAccountCardResponseDto(responseDto, true);
    }

    @Test
    void testGetAccountCardByIdThrowsResourceNotFoundException() {
        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                accountCardService.getAccountCardById(id));

        String expectedMessage = "Account card with ID: "+ id +" not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAccountCardByIdThrowsInvalidRequestException() {
        UUID secId = UUID.randomUUID();
        SecurityUser securityUser = new SecurityUser(secId, Set.of(UserRole.USER_ROLE),
                "password", "JosephSalazar8Mtd2i");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(accountCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountCardForTest(true)));

        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                accountCardService.getAccountCardById(id));

        String expectedMessage = "Invalid Request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeactivateAccountCard() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(accountCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountCardForTest(true)));
        when(accountCardRepository.save(any(AccountCard.class))).thenReturn(buildAccountCardForTest(false));

        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");
        AccountCardResponseDto responseDto = accountCardService.deactivateAccountCard(id);
        assertAccountCardResponseDto(responseDto, false);
    }

    @Test
    void testDeactivateAccountCardThrowsResourceNotFoundException() {
        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                accountCardService.deactivateAccountCard(id));

        String expectedMessage = "Account card with ID: "+ id +" not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeactivateAccountCardThrowsInvalidRequestException() {
        UUID secId = UUID.randomUUID();
        SecurityUser securityUser = new SecurityUser(secId, Set.of(UserRole.USER_ROLE),
                "password", "JosephSalazar8Mtd2i");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(accountCardRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountCardForTest(true)));

        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                accountCardService.deactivateAccountCard(id));

        String expectedMessage = "Invalid Request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private void assertAccountCardResponseDto(AccountCardResponseDto cardResponseDto, boolean expectedIsActive) {
        assertNotNull(cardResponseDto);
        assertEquals(UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d"), cardResponseDto.id());
        assertEquals("1234 5678 1234 5678", cardResponseDto.cardNumber());
        assertEquals("123", cardResponseDto.cvc());
        assertEquals(expectedIsActive, cardResponseDto.isActive());
        assertEquals(LocalDate.now(), cardResponseDto.dateIssued());
        assertEquals(LocalDate.now().plusYears(4), cardResponseDto.expirationDate());
        assertEquals("ANNIE HAYTON", cardResponseDto.cardholderName());
    }

    private AccountCard buildAccountCardForTest(boolean isActive) {
        UUID id = UUID.fromString("f0ce3989-c03f-43ba-b294-27ca63678c8d");
        Account account = buildAccountForTest();
        return AccountCard.builder().id(id).account(account).cardNumber("1234 5678 1234 5678")
                .cvc("123").isActive(isActive).dateIssued(LocalDate.now())
                .expirationDate(LocalDate.now().plusYears(4)).cardholderName("ANNIE HAYTON").build();
    }

    private Account buildAccountForTest() {
        return Account.builder().user(buildUserForTest()).build();
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("88154700-f05c-4442-ace5-38faf864419b");
        return User.builder().id(id).build();
    }

    private SecurityUser buildSecurityUserForTest() {
        UUID id = UUID.fromString("88154700-f05c-4442-ace5-38faf864419b");
        return new SecurityUser(id, Set.of(UserRole.USER_ROLE), "password", "AnnieHayton2Fy3Dq");
    }
}
