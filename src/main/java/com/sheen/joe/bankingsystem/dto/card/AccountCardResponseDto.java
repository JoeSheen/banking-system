package com.sheen.joe.bankingsystem.dto.card;

import java.time.LocalDate;
import java.util.UUID;

public record AccountCardResponseDto(
        UUID id,
        String cardNumber,
        String cvc,
        Boolean isActive,
        LocalDate dateIssued,
        LocalDate expirationDate,
        String cardholderName
) {}
