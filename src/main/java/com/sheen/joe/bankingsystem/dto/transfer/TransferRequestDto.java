package com.sheen.joe.bankingsystem.dto.transfer;

import com.sheen.joe.bankingsystem.entity.TransferCategory;
import com.sheen.joe.bankingsystem.entity.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotBlank
        String senderAccountNumber,
        @NotBlank
        String senderSortCode,
        @NotBlank
        String receiverAccountNumber,
        @NotBlank
        String receiverSortCode,
        @NotBlank
        String receiverFullName,
        @NotNull
        TransferType transferType,
        TransferCategory category,
        String reference,
        @Positive
        BigDecimal amount
) {}
