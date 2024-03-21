package com.sheen.joe.bankingsystem.dto;

import java.util.UUID;

public record AuthenticationResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String token
) {}
