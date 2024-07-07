package com.sheen.joe.bankingsystem.dto.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountSummaryDto(
        UUID id,
        String accountName,
        BigDecimal balance,
        LocalDateTime updatedAt
) {}
