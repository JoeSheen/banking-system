package com.sheen.joe.bankingsystem.dto.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferSummaryDto(
        UUID id,
        BigDecimal amount,
        LocalDateTime timestamp,
        Character symbol
) {}
