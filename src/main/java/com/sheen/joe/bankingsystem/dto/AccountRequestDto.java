package com.sheen.joe.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRequestDto(
        @NotBlank
        String accountName
) {}
