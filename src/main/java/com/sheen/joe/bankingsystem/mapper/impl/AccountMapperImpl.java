package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
                account.getAccountNumber(), account.getCardNumber(), account.getCvc(),
                account.getBalance(), account.getCreatedAt(), account.getUpdatedAt());
    }
}
