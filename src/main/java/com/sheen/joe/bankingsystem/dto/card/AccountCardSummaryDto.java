package com.sheen.joe.bankingsystem.dto.card;

import java.util.UUID;

public record AccountCardSummaryDto(
        UUID id,
        String cardNumber
) {}
