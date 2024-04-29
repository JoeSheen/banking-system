package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.card.AccountCardSummaryDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferSummaryDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferType;
import com.sheen.joe.bankingsystem.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toAccount(AccountRequestDto accountRequestDto) {
        return Account.builder().accountName(accountRequestDto.accountName())
                .balance(BigDecimal.ZERO).closed(false).build();
    }

    @Override
    public AccountResponseDto toAccountResponse(Account account) {
        List<TransferSummaryDto> transferSummaries = toTransferSummaries(account.getSentTransfers(), account.getReceivedTransfers());

        return new AccountResponseDto(account.getId(), account.getAccountName(),
                account.getAccountNumber(), toAccountCardSummary(account.getAccountCard()),
                account.getBalance(), account.getSortCode(), transferSummaries,
                account.getCreatedAt(), account.getUpdatedAt());
    }

    private List<TransferSummaryDto> toTransferSummaries(List<Transfer> sentTransfers, List<Transfer> receivedTransfers) {
        Stream<TransferSummaryDto> sentStream = sentTransfers == null ? Stream.empty() : sentTransfers.stream().map(transfer -> {
            char symbol = computeTransferSummarySymbol(transfer.getTransferType());
            return new TransferSummaryDto(transfer.getId(), transfer.getAmount(), transfer.getTimestamp(), symbol);
        });
        Stream<TransferSummaryDto> receivedStream = receivedTransfers == null ? Stream.empty() : receivedTransfers.stream()
                .map(transfer -> new TransferSummaryDto(transfer.getId(), transfer.getAmount(), transfer.getTimestamp(), '+'));

        return Stream.concat(sentStream, receivedStream).sorted(Comparator.comparing(TransferSummaryDto::timestamp,
                        Comparator.reverseOrder())).toList();
    }

    private AccountCardSummaryDto toAccountCardSummary(AccountCard card) {
        return new AccountCardSummaryDto(card.getId(), card.getCardNumber());
    }

    private char computeTransferSummarySymbol(TransferType type) {
        if (type == TransferType.DEPOSIT) {
            return '+';
        } else {
            return '-';
        }
    }
}
