package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AccountMapper;
import com.sheen.joe.bankingsystem.mapper.impl.AccountMapperImpl;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.AccountServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        AccountMapper accountMapper = new AccountMapperImpl();
        accountService = new AccountServiceImpl(accountRepository, userRepository, accountMapper);
    }

    @Test
    void testCreateAccount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(buildUserForTest()));
        when(accountRepository.save(any(Account.class))).thenReturn(buildAccountForTest("Test Account"));

        AccountRequestDto requestDto = new AccountRequestDto("Test Account");
        AccountResponseDto responseDto = accountService.createAccount(requestDto);

        assertAccountResponseDto(responseDto, "Test Account");
    }

    @Test
    void testUpdateAccount() {
        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(accountRepository.save(any(Account.class))).thenReturn(buildAccountForTest("Updated Test Account"));

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        AccountRequestDto accountRequestDto = new AccountRequestDto("Updated Test Account");
        AccountResponseDto responseDto = accountService.updateAccount(id, accountRequestDto);

        assertAccountResponseDto(responseDto, "Updated Test Account");
    }

    @Test
    void testUpdateAccountThrowsResourceNotFoundException() {
        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        AccountRequestDto accountRequestDto = new AccountRequestDto("Updated Test Account");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                accountService.updateAccount(id, accountRequestDto));

        String expectedMessage = "Account with ID: " + id + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateAccountThrowsInvalidRequestException() {
        UUID sUserId = UUID.fromString("76ef7c14-d1ce-48ed-bf8f-fe131b01cde4");
        SecurityUser securityUser = new SecurityUser(sUserId, Set.of(UserRole.USER_ROLE),
                "password", "KellyHall1hP2zx");

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        AccountRequestDto accountRequestDto = new AccountRequestDto("Updated Test Account");

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                accountService.updateAccount(id, accountRequestDto));

        String expectedMessage = "Invalid account request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void testGetAllUserAccounts() {
        Page<Account> accountResponseDtoPage = new PageImpl<>(List.of(buildAccountForTest("Test Account")));
        when(accountRepository.findAllUserAccounts(anyBoolean(), any(UUID.class), any())).thenReturn(accountResponseDtoPage);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());

        Page<AccountResponseDto> responseDtoPage = accountService.getAllUserAccounts(0, 5,
                false, "updatedAt");

        assertEquals(0, accountResponseDtoPage.getNumber());
        assertEquals(1, accountResponseDtoPage.getNumberOfElements());
        assertAccountResponseDto(responseDtoPage.getContent().get(0), "Test Account");
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        AccountResponseDto responseDto = accountService.getAccountById(id);

        assertAccountResponseDto(responseDto, "Test Account");
    }

    @Test
    void testGetAccountByIdThrowsResourceNotFoundException() {
        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                accountService.getAccountById(id));

        String expectedMessage = "Account with ID: " + id + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAccountByIdThrowsInvalidRequestException() {
        UUID sUserId = UUID.fromString("76ef7c14-d1ce-48ed-bf8f-fe131b01cde4");
        SecurityUser securityUser = new SecurityUser(sUserId, Set.of(UserRole.USER_ROLE),
                "password", "KellyHall1hP2zx");

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                accountService.getAccountById(id));

        String expectedMessage = "Invalid account request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCloseAccount() {
        Account account = buildAccountForTest("Test Account");
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(account));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        Pair<Boolean, String> pair = accountService.closeAccount(id);

        assertTrue(pair.getLeft());
        String expectedMessage = "Account with ID: " + id + " successfully closed";
        String actualMessage = pair.getRight();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCloseAccountWithNoneZeroBalance() {
        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        Pair<Boolean, String> pair = accountService.closeAccount(id);

        assertFalse(pair.getLeft());
        assertEquals("Account balance must be 0.00", pair.getRight());
    }

    @Test
    void testCloseAccountThrowsResourceNotFoundException() {
        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                accountService.closeAccount(id));

        String expectedMessage = "Account with ID: " + id + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCloseAccountThrowsInvalidRequestException() {
        UUID sUserId = UUID.fromString("76ef7c14-d1ce-48ed-bf8f-fe131b01cde4");
        SecurityUser securityUser = new SecurityUser(sUserId, Set.of(UserRole.USER_ROLE),
                "password", "KellyHall1hP2zx");

        when(accountRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildAccountForTest("Test Account")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                accountService.closeAccount(id));

        String expectedMessage = "Invalid account request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private void assertAccountResponseDto(AccountResponseDto accountResponseDto, String expectedAccountName) {
        assertNotNull(accountResponseDto);
        assertEquals(UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5"), accountResponseDto.id());
        assertEquals(expectedAccountName, accountResponseDto.accountName());
        assertEquals("12345678", accountResponseDto.accountNumber());
        assertEquals("1234 5678 1234 5678", accountResponseDto.cardNumber());
        assertEquals("123", accountResponseDto.cvc());
        assertEquals(BigDecimal.TEN, accountResponseDto.balance());
        assertEquals(LocalDateTime.of(2023, 4, 6, 12, 30, 0), accountResponseDto.createdAt());
        assertEquals(LocalDateTime.of(2024, 5, 11, 18, 45, 0), accountResponseDto.updatedAt());
    }

    private SecurityUser buildSecurityUserForTest() {
        UUID id = UUID.fromString("6a428b76-823d-43b0-b6b3-b3e461368862");
        return new SecurityUser(id, Set.of(UserRole.USER_ROLE), "password", "DavidPeterson8vmk0b");
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("6a428b76-823d-43b0-b6b3-b3e461368862");
        LocalDate dateOfBirth = LocalDate.of(1987, Month.MAY, 5);

        return User.builder().id(id).firstName("David").lastName("Peterson")
                .dateOfBirth(dateOfBirth).username("DavidPeterson8vmk0b").build();
    }

    private Account buildAccountForTest(String accountName) {
        UUID id = UUID.fromString("6dabe048-fc46-456c-bc68-66b1380b26a5");
        LocalDateTime createdAt = LocalDateTime.of(2023, 4, 6, 12, 30, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 5, 11, 18, 45, 0);
        User user = buildUserForTest();

        return Account.builder().id(id).accountName(accountName).accountNumber("12345678").
                cardNumber("1234 5678 1234 5678").cvc("123").balance(BigDecimal.TEN).createdAt(createdAt)
                .updatedAt(updatedAt).user(user).build();
    }

}