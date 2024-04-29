package com.sheen.joe.bankingsystem.dto.transfer;

import com.sheen.joe.bankingsystem.entity.TransferCategory;
import com.sheen.joe.bankingsystem.entity.TransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponseDto(
        UUID id,
        TransferType type,
        BigDecimal amount,
        String reference,
        TransferCategory category,
        LocalDateTime timestamp,
        Character symbol
) {}
