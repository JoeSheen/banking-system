package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto accountRequestDto);

    AccountResponseDto updateAccount(UUID id, AccountRequestDto accountRequestDto);

    CollectionResponseDto<AccountResponseDto> getAllUserAccounts(int pageNumber, int pageSize, boolean closed, String sortProperty);

    AccountResponseDto getAccountById(UUID id);

    Pair<Boolean, String> closeAccount(UUID id);

    AccountResponseDto requestNewCardForAccount(UUID id);

}
