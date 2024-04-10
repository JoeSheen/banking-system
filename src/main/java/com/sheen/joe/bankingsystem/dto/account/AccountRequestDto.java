package com.sheen.joe.bankingsystem.dto.account;

import jakarta.validation.constraints.NotBlank;

public record AccountRequestDto(
        @NotBlank
        String accountName
) {}
