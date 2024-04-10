package com.sheen.joe.bankingsystem.dto.user;

import com.sheen.joe.bankingsystem.dto.account.AccountSummaryDto;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String username,
        Set<AccountSummaryDto> accounts
) {}
