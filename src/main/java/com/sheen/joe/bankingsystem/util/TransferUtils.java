package com.sheen.joe.bankingsystem.util;

import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.security.SecurityUtils;

import java.util.UUID;

public class TransferUtils {

    private static final char PLUS = '+';

    private static final char MINUS = '-';

    private TransferUtils() {}

    public static char computeTransferSymbol(Transfer transfer) {
        char symbol;
        UUID senderAccountId = transfer.getSenderAccount().getId();
        Account receiverAccount = transfer.getReceiverAccount();
        if (senderAccountId.equals(receiverAccount.getId())) {
            symbol = transfer.getTransferType() == TransferType.DEPOSIT ? PLUS : MINUS;
        } else {
            UUID receiverUserId = receiverAccount.getUser().getId();
            symbol = receiverUserId.equals(SecurityUtils.getUserIdFromSecurityContext()) ? PLUS : MINUS;
        }
        return symbol;
    }
}
