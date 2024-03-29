package com.sheen.joe.bankingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponseDto(
        UUID id,
        BigDecimal amount,
        LocalDateTime timestamp
) {}
