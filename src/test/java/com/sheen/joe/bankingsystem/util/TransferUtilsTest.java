package com.sheen.joe.bankingsystem.util;

import com.sheen.joe.bankingsystem.entity.*;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferUtilsTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private UUID accountId;

    private UUID securityUserId;

    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        accountId = UUID.fromString("e0c0f7c0-9193-4960-a042-4b692fa8b355");

        securityUserId = UUID.fromString("1c2d4612-a37b-473d-8063-5c339ad480f4");
        securityUser = new SecurityUser(securityUserId, Set.of(UserRole.USER_ROLE), "password", "username");
    }

    @Test
    void testComputeTransferSymbolForDeposit() {
        Account senderAccount = buildAccountForTest(accountId, securityUserId);
        Account receiverAccount = buildAccountForTest(accountId, securityUserId);
        Transfer transfer = buildTransferForTest(TransferType.DEPOSIT, senderAccount, receiverAccount);

        char result = TransferUtils.computeTransferSymbol(transfer);

        assertEquals('+', result);
    }

    @Test
    void testComputeTransferSymbolForWithdraw() {
        Account senderAccount = buildAccountForTest(accountId, securityUserId);
        Account receiverAccount = buildAccountForTest(accountId, securityUserId);
        Transfer transfer = buildTransferForTest(TransferType.WITHDRAW, senderAccount, receiverAccount);

        char result = TransferUtils.computeTransferSymbol(transfer);

        assertEquals('-', result);
    }

    @Test
    void testComputeTransferSymbolForAccountTransferPlus() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        UUID receiverAccountId = UUID.fromString("922c6724-8b07-45a9-a700-a9e0c382d855");
        Account senderAccount = buildAccountForTest(accountId, securityUserId);
        Account receiverAccount = buildAccountForTest(receiverAccountId, securityUserId);
        Transfer transfer = buildTransferForTest(TransferType.BUSINESS, senderAccount, receiverAccount);

        char result = TransferUtils.computeTransferSymbol(transfer);

        assertEquals('+', result);
    }

    @Test
    void testComputeTransferSymbolForAccountTransferMinus() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        UUID receiverUserId = UUID.fromString("528d943b-8755-464b-a6f8-fb470140f361");
        UUID receiverAccountId = UUID.fromString("922c6724-8b07-45a9-a700-a9e0c382d855");
        Account senderAccount = buildAccountForTest(accountId, securityUserId);
        Account receiverAccount = buildAccountForTest(receiverAccountId, receiverUserId);
        Transfer transfer = buildTransferForTest(TransferType.PERSONAL, senderAccount, receiverAccount);

        char result = TransferUtils.computeTransferSymbol(transfer);

        assertEquals('-', result);
    }

    private Transfer buildTransferForTest(TransferType type, Account senderAccount, Account receiverAccount) {
        return Transfer.builder().transferType(type).senderAccount(senderAccount)
                .receiverAccount(receiverAccount).build();
    }

    private Account buildAccountForTest(UUID id, UUID userId) {
        User user = User.builder().id(userId).build();
        return Account.builder().id(id).user(user).build();
    }
}