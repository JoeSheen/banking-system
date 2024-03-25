package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;

public interface AccountMapper {

    Account toAccount(AccountRequestDto accountRequestDto);

    AccountResponseDto toAccountResponse(Account account);

}
