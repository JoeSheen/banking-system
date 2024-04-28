package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.*;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.TransferMapper;
import com.sheen.joe.bankingsystem.mapper.impl.TransferMapperImpl;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.TransferRepository;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    private TransferService transferService;

    private final UUID securityId = UUID.fromString("b14f1bae-d0ba-49cd-bb4c-29a657b586de");

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        TransferMapper mapper = new TransferMapperImpl();
        transferService = new TransferServiceImpl(transferRepository, accountRepository, mapper);
    }

    @Test
    void testCreateDepositTransfer() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(transferRepository.save(any(Transfer.class))).thenReturn(buildTransferForTest());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.DEPOSIT, BigDecimal.TEN);

        TransferResponseDto transferResponseDto = transferService.createTransfer(transferRequestDto);

        assertTransferResponseDto(transferResponseDto);
    }

    @Test
    void testCreateWithdrawTransfer() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(transferRepository.save(any(Transfer.class))).thenReturn(buildTransferForTest());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.WITHDRAW, BigDecimal.TEN);

        TransferResponseDto transferResponseDto = transferService.createTransfer(transferRequestDto);

        assertTransferResponseDto(transferResponseDto);
    }

    @Test
    void testCreateTransferThrowsResourceNotFoundException() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.empty());

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.DEPOSIT, BigDecimal.TEN);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                transferService.createTransfer(transferRequestDto));

        String expectedMessage = "Account not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateTransferThrowsInvalidRequestException() {
        UUID id = UUID.fromString("66445630-04fb-40b8-8bbc-547b0c3cdd80");
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(id));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.WITHDRAW, BigDecimal.TEN);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                transferService.createTransfer(transferRequestDto));

        String expectedMessage = "Invalid Transfer Request";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateTransferWithInsufficientFunds() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        DepositWithdrawTransferRequestDto transferRequestDto = new DepositWithdrawTransferRequestDto("12345678",
                "12-34-56", TransferType.WITHDRAW, new BigDecimal("1000.00"));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                transferService.createTransfer(transferRequestDto));

        String expectedMessage = "Account has insufficient funds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateTransfer() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(transferRepository.save(any(Transfer.class))).thenReturn(buildTransferForTest());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        TransferRequestDto transferRequestDto = new TransferRequestDto("87654321",
                "65-43-21", "12345678", "12-34-56",
                "Maggie Stephens", TransferType.PERSONAL, TransferCategory.ENTERTAINMENT,
                "reference", BigDecimal.TEN);

        TransferResponseDto transferResponseDto = transferService.createTransfer(transferRequestDto);

        assertTransferResponseDto(transferResponseDto);
    }

    @Test
    void testCreateTransferWithIncorrectTransferType() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        TransferRequestDto transferRequestDto = new TransferRequestDto("87654321",
                "65-43-21", "12345678", "12-34-56",
                "Maggie Stephens", TransferType.DEPOSIT, TransferCategory.ENTERTAINMENT,
                "reference", BigDecimal.TEN);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                transferService.createTransfer(transferRequestDto));

        String expectedMessage = "Incorrect Transfer Type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateTransferWithIncorrectReceiverName() {
        when(accountRepository.findByAccountNumberAndSortCode(anyString(), anyString()))
                .thenReturn(Optional.of(buildAccountForTest()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        TransferRequestDto transferRequestDto = new TransferRequestDto("87654321",
                "65-43-21", "12345678", "12-34-56",
                "David Riley", TransferType.PERSONAL, TransferCategory.ENTERTAINMENT,
                "reference", BigDecimal.TEN);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                transferService.createTransfer(transferRequestDto));

        String expectedMessage = "Payee name mismatch";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetTransferById() {
        when(transferRepository.findByIdAndUserId(any(UUID.class), any(UUID.class)))
                .thenReturn(Optional.of(buildTransferForTest()));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        UUID id = UUID.fromString("cb3ab214-7cc2-4bfc-962c-3a0c218aaa09");
        TransferResponseDto transferResponseDto = transferService.getTransferById(id);

        assertTransferResponseDto(transferResponseDto);
    }

    @Test
    void testGetTransferByIdThrowsResourceNotFoundException() {
        when(transferRepository.findByIdAndUserId(any(UUID.class), any(UUID.class)))
                .thenReturn(Optional.empty());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest(securityId));

        UUID id = UUID.fromString("cb3ab214-7cc2-4bfc-962c-3a0c218aaa09");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                transferService.getTransferById(id));

        String expectedMessage = "Transfer with ID: " + id + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private void assertTransferResponseDto(TransferResponseDto transferResponseDto) {
        assertNotNull(transferResponseDto);
        assertEquals(UUID.fromString("cb3ab214-7cc2-4bfc-962c-3a0c218aaa09"), transferResponseDto.id());
        assertEquals(BigDecimal.TEN, transferResponseDto.amount());
        assertEquals(LocalDateTime.of(2024, Month.APRIL, 4, 13, 45, 0), transferResponseDto.timestamp());
    }

    private SecurityUser buildSecurityUserForTest(UUID id) {
        String username = "MaggieStephens7RCfX3";

        return new SecurityUser(id, Set.of(UserRole.USER_ROLE), "password", username);
    }

    private Account buildAccountForTest() {
        UUID accountId = UUID.fromString("92af5b17-510b-4d7c-8752-55a9cdccc7a5");
        UUID userId = UUID.fromString("b14f1bae-d0ba-49cd-bb4c-29a657b586de");

        return Account.builder().id(accountId).balance(new BigDecimal("200.99"))
                .user(User.builder().id(userId).firstName("Maggie").lastName("Stephens").build())
                .build();
    }

    private Transfer buildTransferForTest() {
        UUID id = UUID.fromString("cb3ab214-7cc2-4bfc-962c-3a0c218aaa09");
        LocalDateTime timestamp = LocalDateTime.of(2024, Month.APRIL, 4, 13, 45, 0);
        Account account = buildAccountForTest();

        return Transfer.builder().id(id).amount(BigDecimal.TEN).timestamp(timestamp).senderAccount(account).build();
    }
}
