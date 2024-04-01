package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.TransferMapper;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.TransferRepository;
import com.sheen.joe.bankingsystem.security.SecurityUtils;
import com.sheen.joe.bankingsystem.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    private final TransferMapper transferMapper;

    @Override
    public TransferResponseDto createTransfer(@NonNull TransferRequestDto transferRequestDto) {
        String accountNumber = transferRequestDto.accountNumber();
        Account account = accountRepository.findAccountByAccountNumber(accountNumber).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Unknown Account Number: %s", accountNumber)));
        Transfer transfer = transferMapper.toTransfer(transferRequestDto);
        performTransfer(transfer, account);
        return transferMapper.toTransferResponse(transferRepository.save(transfer));
    }

    @Override
    public TransferResponseDto getTransferById(UUID id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Transfer with ID: %s not found", id)));
        if (isNotUserAccount(transfer.getAccount().getUser().getId())) {
            throw new InvalidRequestException("Invalid Request");
        }
        return transferMapper.toTransferResponse(transfer);
    }

    private void performTransfer(Transfer transfer, Account account) {
        if (isNotUserAccount(account.getUser().getId())) {
            throw new InvalidRequestException("Invalid Request");
        }
        if (isTransferAllowed(transfer.getTransferType(), account.getBalance(), transfer.getAmount())) {
            throw new InvalidRequestException("Insufficient funds to perform transfer");
        }
        updateAccountBalance(account, transfer.getTransferType(), transfer.getAmount());
        transfer.setAccount(account);
    }

    private boolean isNotUserAccount(UUID accountUserId) {
        return !accountUserId.equals(SecurityUtils.getUserIdFromSecurityContext());
    }

    private boolean isTransferAllowed(TransferType type, BigDecimal accountBalance, BigDecimal transferAmount) {
        return type == TransferType.WITHDRAW && accountBalance.compareTo(transferAmount) < 0;
    }

    private void updateAccountBalance(Account account, TransferType type, BigDecimal amount) {
        BigDecimal newBalance;
        switch (type) {
            case DEPOSIT -> newBalance = account.getBalance().add(amount);
            case WITHDRAW -> newBalance = account.getBalance().subtract(amount);
            default -> throw new InvalidRequestException("Transfer type not supported");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
