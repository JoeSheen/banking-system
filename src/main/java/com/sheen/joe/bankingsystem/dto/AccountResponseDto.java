package com.sheen.joe.bankingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String accountName,
        String accountNumber,
        String cardNumber,
        String cvc,
        BigDecimal balance,
        List<TransferSummaryDto> transfers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
