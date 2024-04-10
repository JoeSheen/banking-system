package com.sheen.joe.bankingsystem.dto.transfer;

import com.sheen.joe.bankingsystem.entity.TransferCategory;
import com.sheen.joe.bankingsystem.entity.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotBlank
        String accountNumber,
        @NotNull
        TransferType transferType,
        @Positive
        BigDecimal amount,
        String reference,
        TransferCategory category
) {}
