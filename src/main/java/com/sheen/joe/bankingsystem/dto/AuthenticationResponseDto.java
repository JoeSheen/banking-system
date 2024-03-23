package com.sheen.joe.bankingsystem.dto;

import java.util.UUID;

public record AuthenticationResponseDto(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String token
) {}
