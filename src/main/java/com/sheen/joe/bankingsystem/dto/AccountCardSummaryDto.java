package com.sheen.joe.bankingsystem.dto;

import java.util.UUID;

public record AccountCardSummaryDto(
        UUID id,
        String cardNumber
) {}
