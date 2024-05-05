package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.TransferMapper;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.TransferRepository;
import com.sheen.joe.bankingsystem.security.SecurityUtils;
import com.sheen.joe.bankingsystem.service.TransferService;
import com.sheen.joe.bankingsystem.util.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final TransferMapper transferMapper;

    private static final char MINUS = '-';

    @Override
    @Transactional
    public TransferResponseDto createTransfer(@NonNull DepositWithdrawTransferRequestDto transferRequestDto) {
        Account account = getAccountForTransfer(transferRequestDto.accountNumber(), transferRequestDto.sortCode());
        if (checkAccountOwner(account)) {
            throw new InvalidRequestException("Invalid Transfer Request");
        }
        Transfer transfer = performDepositOrWithdraw(transferRequestDto, account);
        char symbol = TransferUtils.computeTransferSymbol(transfer);
        return transferMapper.toTransferResponse(transferRepository.save(transfer), symbol);
    }

    @Override
    @Transactional
    public TransferResponseDto createTransfer(@NonNull TransferRequestDto transferRequestDto) {
        Account senderAccount = getAccountForTransfer(transferRequestDto.senderAccountNumber(),
                transferRequestDto.senderSortCode());
        if (checkAccountOwner(senderAccount)) {
            throw new InvalidRequestException("Invalid Transfer Request");
        }
        Account receiverAccount = getAccountForTransfer(transferRequestDto.receiverAccountNumber(),
                transferRequestDto.receiverSortCode());
        Transfer transfer = performAccountToAccountTransfer(senderAccount, receiverAccount, transferRequestDto);
        return transferMapper.toTransferResponse(transferRepository.save(transfer), MINUS);
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponseDto getTransferById(UUID id) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        Transfer transfer = transferRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Transfer with ID: %s not found", id)));
        char symbol = TransferUtils.computeTransferSymbol(transfer);
        return transferMapper.toTransferResponse(transfer, symbol);
    }

    private Transfer performAccountToAccountTransfer(Account sender, Account receiver, TransferRequestDto transferDto) {
        if (hasInsufficientAccountBalance(TransferType.WITHDRAW, sender.getBalance(), transferDto.amount())) {
            throw new InvalidRequestException("Account has insufficient funds");
        }
        if (!checkTransferType(transferDto.transferType())) {
            throw new InvalidRequestException("Incorrect Transfer Type");
        }
        if (checkReceiverOwnerName(transferDto.receiverFullName(), receiver.getUser())) {
            throw new InvalidRequestException("Payee name mismatch");
        }
        updateAccountBalance(sender, TransferType.WITHDRAW, transferDto.amount());
        updateAccountBalance(receiver, TransferType.DEPOSIT, transferDto.amount());
        return transferMapper.toTransfer(transferDto, sender, receiver);
    }

    private Transfer performDepositOrWithdraw(DepositWithdrawTransferRequestDto transferRequestDto, Account account) {
        TransferType type = transferRequestDto.transferType();
        BigDecimal amount = transferRequestDto.amount();
        if (hasInsufficientAccountBalance(type, account.getBalance(), amount)) {
            throw new InvalidRequestException("Account has insufficient funds");
        }
        updateAccountBalance(account, type, amount);
        return transferMapper.toTransfer(transferRequestDto, account);
    }

    private void updateAccountBalance(Account account, TransferType type, BigDecimal amount) {
        BigDecimal newAccountBalance;
        switch (type) {
            case DEPOSIT -> newAccountBalance = account.getBalance().add(amount);
            case WITHDRAW -> newAccountBalance = account.getBalance().subtract(amount);
            default -> {
                String message = String.format("Transfer type: %s not supported", type.toString().toLowerCase());
                throw new InvalidRequestException(message);
            }
        }
        account.setBalance(newAccountBalance);
        accountRepository.save(account);
    }

    private boolean hasInsufficientAccountBalance(TransferType type, BigDecimal accountBalance, BigDecimal transferAmount) {
        return type == TransferType.WITHDRAW && accountBalance.compareTo(transferAmount) < 0;
    }

    private boolean checkTransferType(TransferType type) {
        return type == TransferType.BUSINESS || type == TransferType.PERSONAL;
    }

    private Account getAccountForTransfer(String accountNumber, String sortCode) {
        return accountRepository.findByAccountNumberAndSortCode(accountNumber, sortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private boolean checkReceiverOwnerName(String receiverFullName, User receiverUser) {
        String fullName = receiverUser.getFirstName() + " " + receiverUser.getLastName();
        return !fullName.trim().equalsIgnoreCase(receiverFullName);
    }

    private boolean checkAccountOwner(Account account) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        return !account.getUser().getId().equals(userId);
    }

}
