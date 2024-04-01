package com.sheen.joe.bankingsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

public record AccountSummaryDto(UUID id, String accountName, BigDecimal balance, LocalDateTime updatedAt) implements Comparator<AccountSummaryDto> {

    @Override
    public int compare(AccountSummaryDto accountSummaryDto1, AccountSummaryDto accountSummaryDto2) {
        return accountSummaryDto1.updatedAt.compareTo(accountSummaryDto2.updatedAt);
    }
}
