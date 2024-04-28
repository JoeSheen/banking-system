package com.sheen.joe.bankingsystem.dto.transfer;

import com.sheen.joe.bankingsystem.entity.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositWithdrawTransferRequestDto(
        @NotBlank
        String accountNumber,
        @NotBlank
        String sortCode,
        @NotNull
        TransferType transferType,
        @Positive
        BigDecimal amount
) {}
