package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.account.AccountSummaryDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toUserResponse(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getPhoneNumber(), user.getEmail(), user.getUsername(),
                toAccountSummaries(user.getAccounts()));
    }

    private Set<AccountSummaryDto> toAccountSummaries(Set<Account> accounts) {
        if (accounts != null) {
            return accounts.stream().map(account -> new AccountSummaryDto(account.getId(),
                account.getAccountName(), account.getBalance(), account.getUpdatedAt()))
                .sorted(Comparator.comparing(AccountSummaryDto::updatedAt, Comparator.reverseOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return Collections.emptySet();
    }

}
