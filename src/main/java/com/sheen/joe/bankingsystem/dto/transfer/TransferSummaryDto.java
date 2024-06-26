package com.sheen.joe.bankingsystem.dto.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

public record TransferSummaryDto(UUID id, BigDecimal amount, LocalDateTime timestamp, Character symbol) implements Comparator<TransferSummaryDto> {

    @Override
    public int compare(TransferSummaryDto transferSummaryDto1, TransferSummaryDto transferSummaryDto2) {
        return transferSummaryDto1.timestamp.compareTo(transferSummaryDto2.timestamp);
    }
}
