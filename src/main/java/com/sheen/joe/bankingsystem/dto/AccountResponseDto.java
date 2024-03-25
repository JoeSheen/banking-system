package com.sheen.joe.bankingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String accountName,
        String accountNumber,
        String cardNumber,
        String cvc,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
