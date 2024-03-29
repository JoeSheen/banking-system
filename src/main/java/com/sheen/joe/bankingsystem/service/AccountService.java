package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.AccountResponseDto;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto accountRequestDto);

    AccountResponseDto updateAccount(UUID id, AccountRequestDto accountRequestDto);

    Page<AccountResponseDto> getAllUserAccounts(int pageNumber, int pageSize, boolean closed, String sortProperty);

    AccountResponseDto getAccountById(UUID id);

    Pair<Boolean, String> closeAccount(UUID id);

}
