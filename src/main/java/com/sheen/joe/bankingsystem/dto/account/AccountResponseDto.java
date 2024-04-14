package com.sheen.joe.bankingsystem.dto.account;

import com.sheen.joe.bankingsystem.dto.CollectableDtoI;
import com.sheen.joe.bankingsystem.dto.transfer.TransferSummaryDto;
import com.sheen.joe.bankingsystem.dto.card.AccountCardSummaryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String accountName,
        String accountNumber,
        AccountCardSummaryDto card,
        BigDecimal balance,
        List<TransferSummaryDto> transfers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements CollectableDtoI {}
