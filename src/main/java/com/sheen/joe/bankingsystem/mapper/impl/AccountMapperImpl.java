package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.card.AccountCardSummaryDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferSummaryDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toAccount(AccountRequestDto accountRequestDto) {
        return Account.builder().accountName(accountRequestDto.accountName())
                .balance(BigDecimal.ZERO).closed(false).build();
    }

    @Override
    public AccountResponseDto toAccountResponse(Account account) {
        return new AccountResponseDto(account.getId(), account.getAccountName(),
                account.getAccountNumber(), toAccountCardSummary(account.getAccountCard()),
                account.getBalance(), toTransferSummaries(account.getTransfers()),
                account.getCreatedAt(), account.getUpdatedAt());
    }

    private List<TransferSummaryDto> toTransferSummaries(List<Transfer> transfers) {
        if (transfers != null) {
            return transfers.stream().map(transfer -> new TransferSummaryDto(transfer.getId(),
                transfer.getAmount(), transfer.getTimestamp()))
                .sorted(Comparator.comparing(TransferSummaryDto::timestamp,
                        Comparator.reverseOrder())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private AccountCardSummaryDto toAccountCardSummary(AccountCard card) {
        return new AccountCardSummaryDto(card.getId(), card.getCardNumber());
    }
}
